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
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.facade.EnvFacade;
import com.baiyi.caesar.factory.engine.TaskEngineCenter;
import com.baiyi.caesar.factory.engine.TaskEngineHandlerFactory;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.*;
import com.google.common.base.Joiner;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.baiyi.caesar.factory.jenkins.monitor.MonitorHandler.HOST_STATUS_DISABLE;

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
    private TaskEngineCenter jenkinsJobEngineHandler;

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
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    public CsJobBuildServerService csJobBuildServerService;

    @Resource
    private TaskEngineCenter jobEngineCenter;

    @Resource
    public EnvFacade envFacade;

    protected CsApplication queryApplicationById(int applicationId) {
        return csApplicationService.queryCsApplicationById(applicationId);
    }

    /**
     * 限制任务并发
     *
     * @param csCdJob
     * @return
     */
    protected BusinessWrapper<Boolean> tryLimitConcurrentJob(CsCdJob csCdJob) {
        if (isLimitConcurrentJob()) {
            if (csCdJobBuildService.queryLastCdJobBuild(csCdJob.getId()).stream().allMatch(CsCdJobBuild::getFinalized)) {
                return BusinessWrapper.SUCCESS;
            } else {
                return new BusinessWrapper<>(ErrorEnum.JENKINS_LIMIT_CONCURRENT_JOB);
            }
        } else {
            return BusinessWrapper.SUCCESS;
        }
    }

    protected boolean isLimitConcurrentJob() {
        return false;
    }

    /**
     * impl重写
     *
     * @param csApplication
     * @param params
     * @param status
     */
    protected void updateHostStatus(CsApplication csApplication, Map<String, String> params, int status) {
    }

    @Override
    public BusinessWrapper<Boolean> deployment(CsCdJob csJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        BusinessWrapper<Boolean> limitWrapper = tryLimitConcurrentJob(csJob);
        if (!limitWrapper.isSuccess()) return limitWrapper;

        CsApplication csApplication = queryApplicationById(csJob.getApplicationId());
        BusinessWrapper<JobEngineVO.JobEngine> jobEngineWrapper = acqJobEngine(csJob);
        if (!jobEngineWrapper.isSuccess())
            return new BusinessWrapper<>(jobEngineWrapper.getCode(), jobEngineWrapper.getDesc());

        JobEngineVO.JobEngine jobEngine = jobEngineWrapper.getBody();
        raiseJobBuildNumber(csJob); // buildNumber +1
        JobParamDetail jobParamDetail = acqBaseBuildParams(csApplication, csJob, deploymentParam);

        CsCdJobBuild csCdJobBuild = CdJobBuildBuilder.build(csApplication, csJob, jobEngine, jobParamDetail, deploymentParam.getCiBuildId());
        updateHostStatus(csApplication, jobParamDetail.getParams(), HOST_STATUS_DISABLE);
        try {
            JobWithDetails job = jenkinsServerHandler.getJob(jobEngine.getJenkinsInstance().getName(), csCdJobBuild.getJobName()).details();
            QueueReference queueReference = build(job, jobParamDetail.getParams());
        } catch (IOException e) {
            e.printStackTrace();
            updateHostStatus(csApplication, jobParamDetail.getParams(), HOST_STATUS_DISABLE);
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
            saveDetails(context);
            deploymentStartNotify(context); // 通知

            jobEngineCenter.trackBuildTask(context);  // 追踪任务
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BusinessWrapper.SUCCESS;
    }

    protected void saveDetails(DeploymentJobContext context) {
    }

    private void deploymentStartNotify(DeploymentJobContext context) {
        try {
            IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(getKey());
            dingtalkNotify.doNotify(NoticePhase.START.getType(), context);
        } catch (Exception e) {
            log.error("部署消息发送失败(配置不存在)!, cdJobId = {}, buildId = {}", context.getCsCdJob().getId(), context.getJobBuild().getId());
        }
    }

    private void saveCsCdJobBuild(CsCdJobBuild csCdJobBuild) {
        csCdJobBuildService.addCsCdJobBuild(csCdJobBuild); // 写入任务
        // 心跳
        TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.DEPLOYMENT.getType()).trackJobBuildHeartbeat(csCdJobBuild.getId());
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
        // 插入ossPath
        List<CsJobBuildArtifact> artifacts = acqBuildArtifacts(deploymentParam.getCiBuildId());
        if (!CollectionUtils.isEmpty(artifacts))
            params.put("ossPath", artifacts.get(0).getStoragePath());
        try {
            // 取build任务环境变量
            params.put("env", envFacade.queryEnvNameByType(csCiJob.getEnvType()));
        } catch (Exception e) {
            log.error("任务环境未配置！jobName={}", csCiJob.getName());
        }

        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(deploymentParam.getCiBuildId());

        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());

        return JobParamDetail.builder()
                .jenkinsJobParameters(jenkinsJobParameters)
                .params(params)
                .csOssBucket(csOssBucket)
                .jobName(jobName)
                .versionName(StringUtils.isEmpty(deploymentParam.getVersionName()) ? csCiJobBuild.getVersionName() : deploymentParam.getVersionName())
                .versionDesc(StringUtils.isEmpty(deploymentParam.getVersionDesc()) ? csCiJobBuild.getVersionDesc() : deploymentParam.getVersionDesc())
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
        return TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.DEPLOYMENT.getType()).acqJobEngine(csJob.getId());
    }

    @Override
    public void trackJobDeployment(CsCdJobBuild csCdJobBuild) {
        DeploymentJobContext context = acqDeploymentJobContext(csCdJobBuild);
        TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.DEPLOYMENT.getType()).trackJobBuild(context); // 追踪任务
    }

    @Override
    public DeploymentJobContext acqDeploymentJobContext(CsCdJobBuild csCdJobBuild) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(csCdJobBuild.getCdJobId());

        JenkinsJobParameters jenkinsJobParameters = JenkinsUtils.convert(csCdJob.getParameterYaml());
        Map<String, String> params = JenkinsUtils.convert(jenkinsJobParameters);

        JobParamDetail jobParamDetail = JobParamDetail.builder()
                .params(params)
                .build();

        return DeploymentJobContext.builder()
                .csApplication(queryApplicationById(csCdJob.getApplicationId()))
                .csCiJob(csCiJobService.queryCsCiJobById(csCdJob.getCiJobId()))
                .csCdJob(csCdJob)
                .jobBuild(jobDeploymentDecorator.decorator(BeanCopierUtils.copyProperties(csCdJobBuild, CdJobBuildVO.JobBuild.class), 1))
                .jobEngine(acqJobEngineById(csCdJobBuild.getJobEngineId()))
                .jobParamDetail(jobParamDetail)
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
