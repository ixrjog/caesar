package com.baiyi.caesar.factory.jenkins.impl.build;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.builder.jenkins.CiJobBuildBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.JenkinsUtils;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.engine.JenkinsJobEngineHandler;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.google.common.base.Joiner;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.models.GitlabBranch;
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
public abstract class BaseBuildJobHandler implements IBuildJobHandler, InitializingBean {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private JenkinsJobEngineHandler jenkinsJobEngineHandler;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    protected CsApplicationService csApplicationService;

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    private JobBuildDecorator jobBuildDecorator;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private GitlabBranchHandler gitlabBranchHandler;

    @Override
    public BusinessWrapper<Boolean> build(CsCiJob csJob, JobBuildParam.BuildParam buildParam) {
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csJob.getApplicationId());
        BusinessWrapper<JobEngineVO.JobEngine> wrapper = acqJobEngine(csJob);
        if (!wrapper.isSuccess())
            return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        JobEngineVO.JobEngine jobEngine = wrapper.getBody();
        raiseJobBuildNumber(csJob); // buildNumber +1
        JobParamDetail jobParamDetail = acqBaseBuildParams(csApplication, csJob, buildParam);
        GitlabBranch gitlabBranch = acqGitlabBranch(csJob, jobParamDetail.getParams().getOrDefault("branch", ""));
        CsCiJobBuild csCiJobBuild = CiJobBuildBuilder.build(csApplication, csJob, jobEngine, jobParamDetail, gitlabBranch);
        try {
            JobWithDetails job = jenkinsServerHandler.getJob(jobEngine.getJenkinsInstance().getName(), csCiJobBuild.getJobName()).details();
            QueueReference queueReference = build(job, jobParamDetail.getParams());
        } catch (IOException e) {
            e.printStackTrace();
            return new BusinessWrapper<>(100001, "执行任务失败: " + e.getMessage());
        }
        try {
            csCiJobBuild.setParameters(JSON.toJSONString(jobParamDetail.getJenkinsJobParameters()));
            saveCsCiJobBuild(csCiJobBuild);
            JobBuildContext jobBuildContext = JobBuildContext.builder()
                    .csApplication(csApplicationService.queryCsApplicationById(csJob.getApplicationId()))
                    .csCiJob(csJob)
                    .jobBuild(jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class), 1))
                    .jobEngine(jobEngine)
                    .jobParamDetail(jobParamDetail)
                    .build();
            buildStartNotify(jobBuildContext); // 通知
            jenkinsJobEngineHandler.trackJobBuild(jobBuildContext); // 追踪任务
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BusinessWrapper.SUCCESS;
    }

    private void saveCsCiJobBuild(CsCiJobBuild csCiJobBuild) {
        csCiJobBuildService.addCsCiJobBuild(csCiJobBuild); // 写入任务
        jenkinsJobEngineHandler.trackJobBuildHeartbeat(BuildType.BUILD.getType(), csCiJobBuild.getId()); // 心跳
    }

    @Override
    public void trackJobBuild(CsCiJobBuild csCiJobBuild) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(csCiJobBuild.getCiJobId());
        JobBuildContext jobBuildContext = JobBuildContext.builder()
                .csApplication(csApplicationService.queryCsApplicationById(csCiJob.getApplicationId()))
                .csCiJob(csCiJob)
                .jobBuild(jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class), 1))
                .jobEngine(acqJobEngineById(csCiJobBuild.getJobEngineId()))
                .build();
        jenkinsJobEngineHandler.trackJobBuild(jobBuildContext); // 追踪任务
    }

    private GitlabBranch acqGitlabBranch(CsCiJob csCiJob, String branch) {
        try {
            CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(csCiJob.getScmMemberId());
            CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(csApplicationScmMember.getScmId());
            CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
            return gitlabBranchHandler.getBranch(csGitlabInstance.getName(), csGitlabProject.getProjectId(), branch);
        } catch (IOException e) {
            return null;
        }
    }

    private void buildStartNotify(JobBuildContext jobBuildContext) {
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(getKey());
        dingtalkNotify.doNotify(NoticePhase.START.getType(), jobBuildContext);
    }

    private void raiseJobBuildNumber(CsCiJob csJob) {
        csJob.setJobBuildNumber(csJob.getJobBuildNumber() + 1);
        csCiJobService.updateCsCiJob(csJob);
    }

    /**
     * 取任务build参数
     *
     * @param csCiJob
     * @return
     */
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCiJob csCiJob, JobBuildParam.BuildParam buildParam) {
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtils.convert(csCiJob.getParameterYaml());
        Map<String, String> params = JenkinsUtils.convert(jenkinsJobParameters);
        CsApplicationScmMember csApplicationScmMember = applicationFacade.queryScmMemberById(csCiJob.getScmMemberId());
        if (csApplicationScmMember != null)
            params.put("sshUrl", csApplicationScmMember.getScmSshUrl());
        params.put("branch", buildParam.getBranch());
        params.put("applicationName", csApplication.getApplicationKey());
        CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(csCiJob.getOssBucketId());
        params.put("bucketName", csOssBucket.getName());

        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());

        return JobParamDetail.builder()
                .jenkinsJobParameters(jenkinsJobParameters)
                .params(params)
                .csOssBucket(csOssBucket)
                .jobName(jobName)
                .versionName(buildParam.getVersionName())
                .versionDesc(buildParam.getVersionDesc())
                .build();
    }

    @Override
    public String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        // iOS Java Python
        // /应用名/任务名/任务编号/
        CsApplication csApplication = csApplicationService.queryCsApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber = String.valueOf(jobBuild.getJobBuildNumber());
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csJobBuildArtifact.getArtifactFileName());
    }

    private BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCiJob csCiJob) {
        return jenkinsJobEngineHandler.acqJobEngine(csCiJob);
    }

    private JobEngineVO.JobEngine acqJobEngineById(int jobEngineId) {
        return jenkinsJobEngineHandler.acqJobEngineByJobEngineId(jobEngineId);
    }

    private QueueReference build(JobWithDetails job, Map<String, String> params) throws IOException {
        return job.build(params, JenkinsServerHandler.CRUMB_FLAG);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        BuildJobHandlerFactory.register(this);
    }

}
