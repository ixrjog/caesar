package com.baiyi.caesar.factory.jenkins.impl;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.builder.jenkins.CiJobBuildBuilder;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.JenkinsUtils;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.factory.jenkins.IJenkinsJobHandler;
import com.baiyi.caesar.factory.jenkins.JenkinsJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.engine.JenkinsJobEngineHandler;
import com.baiyi.caesar.factory.jenkins.model.JobParamDetail;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:46 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseJenkinsJobHandler implements IJenkinsJobHandler, InitializingBean {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private JenkinsJobEngineHandler jenkinsJobEngineHandler;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsApplicationService csApplicationService;

    @Override
    public BusinessWrapper<Boolean> build(JobBuildParam.CiBuildParam buildParam) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(buildParam.getCiJobId());
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCiJob.getApplicationId());
        BusinessWrapper<CiJobVO.JobEngine> wrapper = acqJobEngine(csCiJob);
        if (!wrapper.isSuccess())
            return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        CiJobVO.JobEngine jobEngine = wrapper.getBody();

        CsCiJobBuild csCiJobBuild = CiJobBuildBuilder.build(csApplication, csCiJob, jobEngine);
        JobWithDetails job = jenkinsServerHandler.getJob(jobEngine.getJenkinsInstance().getName(), csCiJobBuild.getJobName());
        try {
            JobParamDetail jobParamDetail = acqBaseBuildParams(csCiJob);
            QueueReference queueReference = build(job, jobParamDetail.getParams());
            csCiJobBuild.setParameters(JSON.toJSONString(jobParamDetail.getJenkinsJobParameters()));
            csCiJobBuildService.addCsCiJobBuild(csCiJobBuild); // 写入任务
        } catch (IOException e) {
            e.printStackTrace();

        }

        return BusinessWrapper.SUCCESS;
    }

    private JobParamDetail acqBaseBuildParams(CsCiJob csCiJob) {
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtils.convert(csCiJob.getParameterYaml());
        Map<String, String> params = JenkinsUtils.convert(jenkinsJobParameters);
        return JobParamDetail.builder()
                .jenkinsJobParameters(jenkinsJobParameters)
                .params(params)
                .build();
    }


    private BusinessWrapper<CiJobVO.JobEngine> acqJobEngine(CsCiJob csCiJob) {
        return jenkinsJobEngineHandler.acqJobEngine(csCiJob);
    }


    private QueueReference build(JobWithDetails job, Map<String, String> params) throws IOException {
        return job.build(params, JenkinsServerHandler.CRUMB_FLAG);
    }


    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        JenkinsJobHandlerFactory.register(this);
    }

}
