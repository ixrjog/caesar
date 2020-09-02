package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.builder.jenkins.CdJobBuildBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.JenkinsUtils;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.factory.jenkins.engine.JobEngineHandler;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.jenkins.CsJobBuildArtifactService;
import com.google.common.base.Joiner;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/27 4:44 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseDeploymentJobHandler implements IDeploymentJobHandler, InitializingBean {

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private JobEngineHandler jenkinsJobEngineHandler;

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    protected JenkinsServerHandler jenkinsServerHandler;

    @Resource
    protected CsCdJobBuildService csCdJobBuildService;

    @Resource
    protected JobDeploymentDecorator jobDeploymentDecorator;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private CsOssBucketService csOssBucketService;

    protected CsApplication queryApplicationById(int applicationId) {
        return csApplicationService.queryCsApplicationById(applicationId);
    }

    @Override
    public BusinessWrapper<Boolean> deployment(CsCdJob csJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        CsApplication csApplication = queryApplicationById(csJob.getApplicationId());
        BusinessWrapper<JobEngineVO.JobEngine> wrapper = acqJobEngine(csJob);
        if (!wrapper.isSuccess())
            return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        JobEngineVO.JobEngine jobEngine = wrapper.getBody();
        raiseJobBuildNumber(csJob); // buildNumber +1
        JobParamDetail jobParamDetail = acqBaseBuildParams(csApplication, csJob, deploymentParam);

        CsCdJobBuild csCdJobBuild = CdJobBuildBuilder.build(csApplication, csJob, jobEngine, jobParamDetail, deploymentParam.getCiBuildId());
        try {
            JobWithDetails job = jenkinsServerHandler.getJob(jobEngine.getJenkinsInstance().getName(), csCdJobBuild.getJobName()).details();
            QueueReference queueReference = build(job, jobParamDetail.getParams());
        } catch (IOException e) {
            e.printStackTrace();
            return new BusinessWrapper<>(100001, "执行任务失败: " + e.getMessage());
        }
        try {
            csCdJobBuild.setParameters(JSON.toJSONString(jobParamDetail.getJenkinsJobParameters()));
            saveCsCdJobBuild(csCdJobBuild);

            DeploymentJobContext context = DeploymentJobContext.builder()
                    .csApplication(queryApplicationById(csJob.getApplicationId()))
                    .csCiJob(csCiJobService.queryCsCiJobById(csJob.getCiJobId()))
                    .csCdJob(csJob)
                    .jobBuild(jobDeploymentDecorator.decorator(csCdJobBuild, 1))
                    .jobEngine(jobEngine)
                    .jobParamDetail(jobParamDetail)
                    .build();
            deploymentStartNotify(context); // 通知
            jenkinsJobEngineHandler.trackJobBuild(context); // 追踪任务
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BusinessWrapper.SUCCESS;
    }

    private void deploymentStartNotify(DeploymentJobContext context) {
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(getKey());
        dingtalkNotify.doNotify(NoticePhase.START.getType(), context);
    }

    private void saveCsCdJobBuild(CsCdJobBuild csCdJobBuild) {
        csCdJobBuildService.addCsCdJobBuild(csCdJobBuild); // 写入任务
        jenkinsJobEngineHandler.trackJobBuildHeartbeat(BuildType.DEPLOYMENT.getType(), csCdJobBuild.getId()); // 心跳
    }

    private QueueReference build(JobWithDetails job, Map<String, String> params) throws IOException {
        return job.build(params, JenkinsServerHandler.CRUMB_FLAG);
    }

    /**
     * 取任务build参数
     *
     * @param csCdJob
     * @return
     */
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtils.convert(csCdJob.getParameterYaml());
        Map<String, String> params = JenkinsUtils.convert(jenkinsJobParameters);
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(csCdJob.getCiJobId());

        params.put("applicationName", csApplication.getApplicationKey());
        CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(csCiJob.getOssBucketId());
        params.put("bucketName", csOssBucket.getName());

        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());

        return JobParamDetail.builder()
                .jenkinsJobParameters(jenkinsJobParameters)
                .params(params)
                .csOssBucket(csOssBucket)
                .jobName(jobName)
                .versionName(deploymentParam.getVersionName())
                .versionDesc(deploymentParam.getVersionDesc())
                .build();
    }

    protected List<CsJobBuildArtifact> acqBuildArtifacts(int ciBuildId) {
        return filterBuildArtifacts(csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(BuildType.BUILD.getType(), ciBuildId));
    }

    protected List<CsJobBuildArtifact> filterBuildArtifacts(List<CsJobBuildArtifact> artifacts) {
        return artifacts;
    }

    private void raiseJobBuildNumber(CsCdJob csJob) {
        csJob.setJobBuildNumber(csJob.getJobBuildNumber() + 1);
        csCdJobService.updateCsCdJob(csJob);
    }

    private BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCdJob csJob) {
        return jenkinsJobEngineHandler.acqJobEngine(csJob);
    }

    @Override
    public void trackJobDeployment(CsCdJobBuild csCdJobBuild) {
        DeploymentJobContext context = acqDeploymentJobContext(csCdJobBuild);
        jenkinsJobEngineHandler.trackJobBuild(context); // 追踪任务
    }

    @Override
    public DeploymentJobContext acqDeploymentJobContext(CsCdJobBuild csCdJobBuild) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(csCdJobBuild.getCdJobId());
        return DeploymentJobContext.builder()
                .csApplication(queryApplicationById(csCdJob.getApplicationId()))
                .csCiJob(csCiJobService.queryCsCiJobById(csCdJob.getCiJobId()))
                .csCdJob(csCdJob)
                .jobBuild(jobDeploymentDecorator.decorator(BeanCopierUtils.copyProperties(csCdJobBuild, CdJobBuildVO.JobBuild.class), 1))
                .jobEngine(acqJobEngineById(csCdJobBuild.getJobEngineId()))
                .build();
    }

    @Override
    public String acqOssPath(CdJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        // Android
        // /应用名/任务名/任务编号/
        CsApplication csApplication = queryApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber = String.valueOf(jobBuild.getJobBuildNumber());
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csJobBuildArtifact.getArtifactFileName());
    }

    private JobEngineVO.JobEngine acqJobEngineById(int jobEngineId) {
        return jenkinsJobEngineHandler.acqJobEngineByJobEngineId(jobEngineId);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        DeploymentJobHandlerFactory.register(this);
    }
}
