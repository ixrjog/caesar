package com.baiyi.caesar.facade.jenkins.factory.impl;

import com.baiyi.caesar.builder.jenkins.JobEngineBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/11/18 1:30 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class DeploymentJobEngine<T> extends BaseJobEngine<T> {

    @Resource
    private CsCdJobService csCdJobService;

    @Override
    public Integer getKey() {
        return BuildType.DEPLOYMENT.getType();
    }

    protected T acqJob(int jobId) {
        return (T) csCdJobService.queryCsCdJobById(jobId);
    }

    protected CsApplication acqApplication(T job) {
        CsCdJob csCdJob = (CsCdJob) job;
        return csApplicationService.queryCsApplicationById(csCdJob.getApplicationId());
    }

    protected boolean tryJenkinsEngine(int jobId, ApplicationVO.Engine engine) {
        if (csJobEngineService.queryCsJobEngineByUniqueKey(BuildType.DEPLOYMENT.getType(), jobId, engine.getJenkinsInstanceId()) == null)
            return true;
        return !jobEngineHandler.tryJenkinsInstanceActive(engine.getJenkinsInstanceId());
    }

    protected boolean tryJenkinsEngine(int jobId, CsJobEngine engine) {
        if (csJobEngineService.queryCsJobEngineByUniqueKey(BuildType.BUILD.getType(), jobId, engine.getJenkinsInstanceId()) == null)
            return true;
        return !jobEngineHandler.tryJenkinsInstanceActive(engine.getJenkinsInstanceId());
    }

    protected void createJobEngine(CsApplication csApplication, T job, ApplicationVO.Engine engine) {
        CsCdJob csCdJob = (CsCdJob) job;
        if (!tryJenkinsEngine(csCdJob.getId(), engine))
            return;
        // 创建JenkinsJob
        try {
            CsJobEngine csJobEngine = JobEngineBuilder.build(csApplication, csCdJob, engine);
            createJobEngine(csJobEngine, csCdJob.getJobTplId(), engine);
            csJobEngineService.addCsJobEngine(csJobEngine);
        } catch (IOException e) {
            log.error("创建任务引擎错误，jenkinsInstanceId = {}, csCdJobName = {};", engine.getJenkinsInstanceId(), csCdJob.getName());
        }
    }

    protected void updateJobEngine(CsApplication csApplication, T job, CsJobEngine csJobEngine) {
        CsCdJob csCdJob = (CsCdJob) job;
        if (tryJenkinsEngine(csCdJob.getId(), csJobEngine))
            return;
        // 更新JenkinsJob
        try {
            CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(csJobEngine.getJenkinsInstanceId());
            updateJobEngine(csJobEngine, csCdJob.getJobTplId(), csJenkinsInstance.getName());
            csJobEngineService.updateCsJobEngine(csJobEngine);
        } catch (IOException e) {
            log.error("更新任务引擎错误，jenkinsInstanceId = {}, csCiJobName = {};",csJobEngine.getJenkinsInstanceId(), csCdJob.getName());
        }
    }

}