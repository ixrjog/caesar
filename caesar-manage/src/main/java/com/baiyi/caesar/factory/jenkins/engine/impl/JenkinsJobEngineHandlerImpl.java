package com.baiyi.caesar.factory.jenkins.engine.impl;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.CiJobEngineDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.facade.jenkins.JenkinsCiJobFacade;
import com.baiyi.caesar.factory.jenkins.engine.JenkinsJobEngineHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsJobHandler;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/8/6 10:56 上午
 * @Version 1.0
 */
@Component
public class JenkinsJobEngineHandlerImpl implements JenkinsJobEngineHandler {

    @Resource
    private CsCiJobEngineService csCiJobEngineService;

    @Resource
    private JenkinsCiJobFacade jenkinsCiJobFacade;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private CiJobEngineDecorator ciJobEngineDecorator;

    @Resource
    private JenkinsJobHandler jenkinsJobHandler;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Override
    public BusinessWrapper<CiJobVO.JobEngine> acqJobEngine(CsCiJob csCiJob) {
        List<CsCiJobEngine> csCiJobEngines = queryCiJobEngine(csCiJob.getId());
        if (CollectionUtils.isEmpty(csCiJobEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_ENGINE_NOT_CONFIGURED); // 工作引擎未配置
        List<CsCiJobEngine> activeEngines = csCiJobEngines.stream().filter(e ->
                tryJenkinsInstanceActive(e.getJenkinsInstanceId())
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(activeEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_NO_ENGINES_AVAILABLE); // 没有可用的工作引擎

        return new BusinessWrapper<>(buildJobEngine(activeEngines));
    }

    private CiJobVO.JobEngine buildJobEngine(List<CsCiJobEngine> activeEngines) {
        Random random = new Random();
        int n = random.nextInt(activeEngines.size());
        CsCiJobEngine csCiJobEngine = activeEngines.get(n);
        return ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobEngine, CiJobVO.JobEngine.class));
    }

    private boolean tryJenkinsInstanceActive(int jenkinsInstanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jenkinsInstanceId);
        if (csJenkinsInstance == null) return false;
        return csJenkinsInstance.getIsActive();
    }

    private List<CsCiJobEngine> queryCiJobEngine(int ciJobId) {
        List<CsCiJobEngine> csCiJobEngines = csCiJobEngineService.queryCsCiJobEngineByJobId(ciJobId);
        if (CollectionUtils.isEmpty(csCiJobEngines))
            jenkinsCiJobFacade.createJobEngine(ciJobId);
        return csCiJobEngineService.queryCsCiJobEngineByJobId(ciJobId);
    }

    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void trackJobBuild(CsCiJobBuild csCiJobBuild, CiJobVO.JobEngine jobEngine, JobWithDetails job) {
        while (true) {
            try {
                Build build = jenkinsJobHandler.getJobBuildByNumber(job, csCiJobBuild.getEngineBuildNumber());
                BuildWithDetails buildWithDetails = build.details();
                recordJobEngine(job, jobEngine);
                if (buildWithDetails.isBuilding()) {
                    TimeUnit.SECONDS.sleep(5); // 执行中
                } else {
                    // 任务完成
                    recordJobBuild(csCiJobBuild, buildWithDetails);
                    break;
                }
            } catch (RuntimeException e) {
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recordJobEngine(JobWithDetails job, CiJobVO.JobEngine jobEngine) {
        boolean isChanged = false;
        if (job.getNextBuildNumber() != jobEngine.getNextBuildNumber()) {
            jobEngine.setNextBuildNumber(job.getNextBuildNumber());
            isChanged = true;
        }
        if (job.getLastBuild() != null && job.getLastBuild().getNumber() != jobEngine.getLastBuildNumber()) {
            jobEngine.setLastBuildNumber(jobEngine.getLastBuildNumber());
            isChanged = true;
        }
        if (isChanged)
            csCiJobEngineService.updateCsCiJobEngine(BeanCopierUtils.copyProperties(jobEngine, CsCiJobEngine.class));
    }

    /**
     * 记录构建信息
     */
    private void recordJobBuild(CsCiJobBuild csCiJobBuild, BuildWithDetails buildWithDetails) {
        csCiJobBuild.setBuildStatus(buildWithDetails.getResult().name());
        csCiJobBuild.setEndTime(new Date());
        csCiJobBuild.setFinalized(true);
        csCiJobBuildService.updateCsCiJobBuild(csCiJobBuild);

        List<Artifact> artifacts = buildWithDetails.getArtifacts();
    }
}