package com.baiyi.caesar.facade.jenkins.factory.impl;

import com.baiyi.caesar.builder.jenkins.JobEngineBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/11/18 11:37 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class BuildJobEngine<T> extends BaseJobEngine<T> {

    @Resource
    private CsCiJobService csCiJobService;

    @Override
    public Integer getKey() {
        return BuildType.BUILD.getType();
    }

    protected T acqJob(int jobId) {
        return (T) csCiJobService.queryCsCiJobById(jobId);
    }

    protected CsApplication acqApplication(T job) {
        CsCiJob csCiJob = (CsCiJob) job;
        return csApplicationService.queryCsApplicationById(csCiJob.getApplicationId());
    }

    protected void createJobEngine(CsApplication csApplication, T job, ApplicationVO.Engine engine) {
        CsCiJob csCiJob = (CsCiJob) job;
        if (tryJenkinsEngine(csCiJob.getId(), engine))
            return;
        // 创建JenkinsJob
        try {
            CsJobEngine csJobEngine = JobEngineBuilder.build(csApplication, csCiJob, engine);
            createJobEngine(csJobEngine, csCiJob.getJobTplId(), engine);
            csJobEngineService.addCsJobEngine(csJobEngine);
        } catch (IOException e) {
            log.error("创建任务引擎错误，jenkinsInstanceId = {}, csCiJobName = {};", engine.getJenkinsInstanceId(), csCiJob.getName());
        }
    }

    protected boolean tryJenkinsEngine(int jobId, ApplicationVO.Engine engine) {
        if (csJobEngineService.queryCsJobEngineByUniqueKey(BuildType.BUILD.getType(), jobId, engine.getJenkinsInstanceId()) == null)
            return true;
        return !jobEngineHandler.tryJenkinsInstanceActive(engine.getJenkinsInstanceId());
    }

    protected void updateJobEngine(CsApplication csApplication, T job, ApplicationVO.Engine engine) {
        CsCiJob csCiJob = (CsCiJob) job;

        if (tryJenkinsEngine(csCiJob.getId(), engine))
            return;
        // 更新JenkinsJob
        try {
            CsJobEngine csJobEngine = JobEngineBuilder.build(csApplication, csCiJob, engine);
            updateJobEngine(csJobEngine, csCiJob.getJobTplId(), engine);
            csJobEngineService.updateCsJobEngine(csJobEngine);
        } catch (IOException e) {
            log.error("更新任务引擎错误，jenkinsInstanceId = {}, csCiJobName = {};", engine.getJenkinsInstanceId(), csCiJob.getName());
        }
    }


}
