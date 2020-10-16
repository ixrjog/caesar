package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.builder.jenkins.JobEngineBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.ApplicationEngineDecorator;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.factory.jenkins.engine.JobEngineHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/3 4:33 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsJobFacade {

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private JobEngineDecorator ciJobEngineDecorator;

    @Resource
    private ApplicationEngineDecorator applicationEngineDecorator;

    @Resource
    private JobEngineHandler jobEngineHandler;

    /**
     * 创建Job引擎配置
     *
     * @param jobId
     */
    public void createJobEngine(int buildType, int jobId) {
        if (buildType == BuildType.BUILD.getType()) {
            createJobBuildEngine(jobId);
        } else {
            createJobDeploymentEngine(jobId);
        }
    }

    public void createJobBuildEngine(int jobId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(jobId);
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCiJob.getApplicationId());
        List<ApplicationVO.Engine> engines = acqEngines(csApplication);
        engines.forEach(e ->
                createJobEngine(csApplication, csCiJob, e)
        );
    }

    public void createJobDeploymentEngine(int jobId) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(jobId);
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCdJob.getApplicationId());
        List<ApplicationVO.Engine> engines = acqEngines(csApplication);
        engines.forEach(e ->
                createJobEngine(csApplication, csCdJob, e)
        );
    }

    private List<ApplicationVO.Engine> acqEngines(CsApplication csApplication) {
        List<ApplicationVO.Engine> engines;
        if (csApplication.getEngineType() == 0) {
            engines = csJenkinsInstanceService.queryAll()
                    .stream().map(e -> {
                        ApplicationVO.Engine engine = new ApplicationVO.Engine();
                        engine.setApplicationId(csApplication.getId());
                        engine.setJenkinsInstanceId(e.getId());
                        return applicationEngineDecorator.decorator(engine, 1);
                    }).collect(Collectors.toList());
        } else {
            // 应用引擎配置
            engines = applicationFacade.queryApplicationEngineByApplicationId(csApplication.getId());
        }
        return engines;
    }

    private void createJobEngine(CsApplication csApplication, CsCiJob csCiJob, ApplicationVO.Engine engine) {
        if (csJobEngineService.queryCsJobEngineByUniqueKey(BuildType.BUILD.getType(), csCiJob.getId(), engine.getJenkinsInstanceId()) != null)
            return;
        if (jobEngineHandler.tryJenkinsInstanceActive(engine.getJenkinsInstanceId())) {
            // 创建JenkinsJob
            try {
                CsJobEngine csJobEngine = JobEngineBuilder.build(csApplication, csCiJob, engine);
                createJobEngine(csJobEngine, csCiJob.getJobTplId(), engine);
                csJobEngineService.addCsJobEngine(csJobEngine);
            } catch (IOException e) {
                log.error("创建任务引擎错误，jenkinsInstanceId = {}, csCiJobName = {};", engine.getJenkinsInstanceId(), csCiJob.getName());
            }
        }
    }

    private void createJobEngine(CsApplication csApplication, CsCdJob csCdJob, ApplicationVO.Engine engine) {
        if (csJobEngineService.queryCsJobEngineByUniqueKey(BuildType.DEPLOYMENT.getType(), csCdJob.getId(), engine.getJenkinsInstanceId()) != null)
            return;
        if (jobEngineHandler.tryJenkinsInstanceActive(engine.getJenkinsInstanceId())) {
            // 创建JenkinsJob
            try {
                CsJobEngine csJobEngine = JobEngineBuilder.build(csApplication, csCdJob, engine);
                createJobEngine(csJobEngine, csCdJob.getJobTplId(), engine);
                csJobEngineService.addCsJobEngine(csJobEngine);
            } catch (IOException e) {
                log.error("创建任务引擎错误，jenkinsInstanceId = {}, csCdJobName = {};", engine.getJenkinsInstanceId(), csCdJob.getName());
            }
        }
    }

    private void createJobEngine(CsJobEngine csJobEngine, int jobTplId, ApplicationVO.Engine engine) throws IOException {
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(jobTplId);
        if (csJobTpl != null) {
            jenkinsServerHandler.createJob(engine.getInstance().getName(), csJobEngine.getName(), csJobTpl.getTplContent());
            if (jenkinsServerHandler.getJob(engine.getInstance().getName(), csJobEngine.getName()) != null) {
                csJobEngine.setTplVersion(csJobTpl.getTplVersion());
                csJobEngine.setTplHash(csJobTpl.getTplHash());
                // csJobEngineService.updateCsJobEngine(csJobEngine);
            }
        }
    }

    public List<JobEngineVO.JobEngine> queryJobEngine(int buildType, int jobId) {
        List<CsJobEngine> list = csJobEngineService.queryCsJobEngineByJobId(buildType, jobId);
        return list.stream().map(e ->
                ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(e, JobEngineVO.JobEngine.class))
        ).collect(Collectors.toList());
    }
}
