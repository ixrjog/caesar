package com.baiyi.caesar.factory.jenkins.impl.cd;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.factory.jenkins.CdJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.ICdJobHandler;
import com.baiyi.caesar.factory.jenkins.engine.JenkinsJobEngineHandler;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/27 4:44 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseCdJobHandler implements ICdJobHandler, InitializingBean {


    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private JenkinsJobEngineHandler jenkinsJobEngineHandler;


    @Resource
    protected CsApplicationService csApplicationService;


    @Override
    public BusinessWrapper<Boolean> deployment(CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
//        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCdJob.getApplicationId());
//        BusinessWrapper<CdJobVO.JobEngine> wrapper = acqJobEngine(csCdJob);
//        if (!wrapper.isSuccess())
//            return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
//        CdJobVO.JobEngine jobEngine = wrapper.getBody();
//        raiseCsCdJobBuildNumber(csCdJob); // buildNumber +1
//        JobParamDetail jobParamDetail = acqBaseDeploymentParams(csApplication, csCdJob, deploymentParam);
//
//        CsCdJobBuild csCdJobBuild = CdJobBuildBuilder.build(csApplication, csCdJob, jobEngine, jobParamDetail);
//        try {
//            JobWithDetails job = jenkinsServerHandler.getJob(jobEngine.getJenkinsInstance().getName(), csCdJobBuild.getJobName()).details();
//            QueueReference queueReference = build(job, jobParamDetail.getParams());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new BusinessWrapper<>(100001, "执行任务失败: " + e.getMessage());
//        }
//        try {
//            csCdJobBuild.setParameters(JSON.toJSONString(jobParamDetail.getJenkinsJobParameters()));
//            saveCsCdJobBuild(csCdJobBuild);
//            JobDeploymentContext jobDeploymentContext = JobDeploymentContext.builder()
//                    .csApplication(csApplicationService.queryCsApplicationById(csCdJob.getApplicationId()))
//                    .csCdJob(csCdJob)
//                    .jobBuild(jobDeploymentDecorator.decorator(BeanCopierUtils.copyProperties(csCdJobBuild, CdJobBuildVO.JobBuild.class), 1))
//                    .jobEngine(jobEngine)
//                    .jobParamDetail(jobParamDetail)
//                    .build();
//            //  doNotifyByBuildStart(jobBuildContext); // 通知
//            jenkinsJobEngineHandler.trackJobDeployment(jobDeploymentContext); // 追踪任务
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void trackJobDeployment(CsCdJobBuild csCdJobBuild) {
    }




    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        CdJobHandlerFactory.register(this);
    }
}
