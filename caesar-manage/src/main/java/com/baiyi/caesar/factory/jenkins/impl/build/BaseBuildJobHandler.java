package com.baiyi.caesar.factory.jenkins.impl.build;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.builder.jenkins.CiJobBuildBuilder;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.exception.build.BuildRuntimeException;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.JenkinsUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.EnvFacade;
import com.baiyi.caesar.factory.engine.TaskEngineCenter;
import com.baiyi.caesar.factory.engine.TaskEngineHandlerFactory;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.builder.JenkinsJobParamsBuilder;
import com.baiyi.caesar.factory.jenkins.builder.JenkinsJobParamsMap;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.JobParametersContext;
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
import org.apache.commons.lang3.StringUtils;
import org.gitlab.api.models.GitlabBranch;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

import static com.baiyi.caesar.common.base.Build.*;
import static com.baiyi.caesar.common.base.Global.BRANCH;
import static com.baiyi.caesar.common.base.Global.EXTEND;
import static com.baiyi.caesar.factory.jenkins.monitor.MonitorHandler.HOST_STATUS_DISABLE;
import static com.baiyi.caesar.factory.jenkins.monitor.MonitorHandler.HOST_STATUS_ENABLE;

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
    private TaskEngineCenter jenkinsJobEngineHandler;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    private JobBuildDecorator jobBuildsDecorator;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private GitlabBranchHandler gitlabBranchHandler;

    @Resource
    private EnvFacade envFacade;

    @Resource
    private TaskEngineCenter taskEngineCenter;

    public static final boolean SEND_DINGTALK_MESSAGE = false;

    protected CsApplication queryApplicationById(int applicationId) {
        return csApplicationService.queryCsApplicationById(applicationId);
    }

    protected boolean isRollback(JobBuildParam.BuildParam buildParam) {
        if (buildParam.getIsRollback() == null || !buildParam.getIsRollback()) return false;
        return buildParam.getParamMap().containsKey(ROLLBACK_JOB_BUILD_ID);
    }

    /**
     * 限制任务并发
     *
     * @param csCiJob
     * @return
     */
    protected void tryLimitConcurrentJob(CsCiJob csCiJob) throws BuildRuntimeException {
        if (isLimitConcurrentJob())
            if (!csCiJobBuildService.queryLatestCiJobBuildByCiJobId(csCiJob.getId()).stream().allMatch(CsCiJobBuild::getFinalized))
                throw new BuildRuntimeException(ErrorEnum.JENKINS_LIMIT_CONCURRENT_JOB);
    }

    protected boolean isLimitConcurrentJob() {
        return false;
    }

    @Override
    public void build(CsCiJob buildJob, String username) {
        raiseJobBuildNumber(buildJob); // buildNumber +1
        JobParametersContext jobParamDetail = buildJobParametersContext(buildJob);
        build(buildJob, jobParamDetail, username, SEND_DINGTALK_MESSAGE);
    }

    @Override
    public BusinessWrapper<Boolean> build(CsCiJob buildJob, JobBuildParam.BuildParam buildParam) {
        tryLimitConcurrentJob(buildJob);
        raiseJobBuildNumber(buildJob); // buildNumber +1
        JobParametersContext context = buildJobParametersContext(buildJob, buildParam);
        return build(buildJob, context, SessionUtil.getUsername(), buildParam.getIsSilence());
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

    private BusinessWrapper<Boolean> build(CsCiJob csCiJob, JobParametersContext parametersContext, String username, Boolean isSilence) {
        BusinessWrapper<JobEngineVO.JobEngine> jobEngineWrapper = acqJobEngine(csCiJob);
        if (!jobEngineWrapper.isSuccess())
            return new BusinessWrapper<>(jobEngineWrapper.getCode(), jobEngineWrapper.getDesc());
        JobEngineVO.JobEngine jobEngine = jobEngineWrapper.getBody();
        CsGitlabProject csGitlabProject = acqGitlabProjectByScmMemberId(csCiJob.getScmMemberId());
        GitlabBranch gitlabBranch = acqGitlabBranch(csGitlabProject, parametersContext.getParams().getOrDefault(BRANCH, ""));
        CsCiJobBuild csCiJobBuild = CiJobBuildBuilder.build(parametersContext.getApplication(), csCiJob, jobEngine, parametersContext, gitlabBranch, username, isSilence);
        updateHostStatus(parametersContext.getApplication(), parametersContext.getParams(), HOST_STATUS_DISABLE);
        try {
            JobWithDetails job = jenkinsServerHandler.getJob(jobEngine.getJenkinsInstance().getName(), csCiJobBuild.getJobName()).details();
            if (job == null) {
                updateHostStatus(parametersContext.getApplication(), parametersContext.getParams(), HOST_STATUS_ENABLE);
                return new BusinessWrapper<>(100001, "Jenkins引擎故障，无法获取任务详情");
            }
            QueueReference queueReference = build(job, parametersContext.getParams());
        } catch (IOException e) {
            e.printStackTrace();
            return new BusinessWrapper<>(100001, "执行任务失败: " + e.getMessage());
        }
        try {
            csCiJobBuild.setParameters(JSON.toJSONString(parametersContext.getJenkinsJobParameters()));
            saveCsCiJobBuild(csCiJobBuild);
            BuildJobContext context = BuildJobContext.builder()
                    .csApplication(queryApplicationById(csCiJob.getApplicationId()))
                    .csCiJob(csCiJob)
                    .csGitlabProject(csGitlabProject)
                    .jobBuild(jobBuildsDecorator.decorator(csCiJobBuild, EXTEND))
                    .jobEngine(jobEngine)
                    .jobParamDetail(parametersContext)
                    .build();
            buildStartNotify(context); // 通知
            taskEngineCenter.trackBuildTask(context);  // 追踪任务
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BusinessWrapper.SUCCESS;
    }

    private void saveCsCiJobBuild(CsCiJobBuild csCiJobBuild) {
        csCiJobBuildService.addCsCiJobBuild(csCiJobBuild); // 写入任务
        // 心跳
        TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.BUILD.getType()).trackJobBuildHeartbeat(csCiJobBuild.getId());
    }

    @Override
    public void trackJobBuild(CsCiJobBuild csCiJobBuild) {
        BuildJobContext context = buildJobContext(csCiJobBuild);
        TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.BUILD.getType()).trackJobBuild(context); // 追踪任务
    }

    @Override
    public BuildJobContext buildJobContext(CsCiJobBuild csCiJobBuild) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(csCiJobBuild.getCiJobId());
        return BuildJobContext.builder()
                .csApplication(queryApplicationById(csCiJob.getApplicationId()))
                .csCiJob(csCiJob)
                .jobBuild(jobBuildsDecorator.decorator(csCiJobBuild, EXTEND))
                .jobEngine(acqJobEngineById(csCiJobBuild.getJobEngineId()))
                .build();
    }

    private GitlabBranch acqGitlabBranch(CsGitlabProject csGitlabProject, String branch) {
        try {
            CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
            return gitlabBranchHandler.getBranch(csGitlabInstance.getName(), csGitlabProject.getProjectId(), branch);
        } catch (IOException e) {
            return null;
        }
    }

    private CsGitlabProject acqGitlabProjectByScmMemberId(int scmMemberId) {
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(scmMemberId);
        return csGitlabProjectService.queryCsGitlabProjectById(csApplicationScmMember.getScmId());
    }

    private void buildStartNotify(BuildJobContext context) {
        if (context.getJobBuild().getIsSilence()) // 消息静默
            return;
        try {
            IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(getKey());
            dingtalkNotify.doNotify(NoticePhase.START.getType(), context);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("部署消息发送失败(配置不存在)!, cdJobId = {}, buildId = {}", context.getCsCiJob().getId(), context.getJobBuild().getId());
        }
    }

    private void raiseJobBuildNumber(CsCiJob csJob) {
        csJob.setJobBuildNumber(csJob.getJobBuildNumber() + 1);
        csCiJobService.updateCsCiJob(csJob);
    }

    protected CsCiJobBuild queryCiJobBuildById(int id) {
        return csCiJobBuildService.queryCiJobBuildById(id);
    }


    protected JobParametersContext buildJobParametersContext(CsCiJob buildJob, JobBuildParam.BuildParam buildParam) {
        JobParametersContext context = buildJobParametersContext(buildJob);
        if (!StringUtils.isEmpty(buildParam.getBranch()))
            context.getParams().put(BRANCH, buildParam.getBranch());
        // 回滚
        if (buildParam.getIsRollback() != null && buildParam.getIsRollback()) {
            context.setIsRollback(true);
        } else {
            context.setVersionName(buildParam.getVersionName());
            context.setVersionDesc(buildParam.getVersionDesc());
        }
        return context;
    }

    private JobParametersContext buildJobParametersContext(CsCiJob csCiJob) {
        CsApplication csApplication = queryApplicationById(csCiJob.getApplicationId());
        CsApplicationScmMember csApplicationScmMember = applicationFacade.queryScmMemberById(csCiJob.getScmMemberId());
        CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(csCiJob.getOssBucketId());
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtil.convert(csCiJob.getParameterYaml());

        JenkinsJobParamsMap jenkinsJobParamsMap = JenkinsJobParamsBuilder.newBuilder()
                .paramEntries(JenkinsUtil.convert(jenkinsJobParameters))
                .paramEntry(SSH_URL, csApplicationScmMember != null ? csApplicationScmMember.getScmSshUrl() : null)
                .paramEntry(BRANCH, csCiJob.getBranch())
                .paramEntry(APPLICATION_NAME, csApplication.getApplicationKey())
                .paramEntry(BUCKET_NAME, csOssBucket.getName())
                .paramEntry(ENV, envFacade.queryEnvNameByType(csCiJob.getEnvType()))
                .paramEntry(JOB_BUILD_NUMBER, String.valueOf(csCiJob.getJobBuildNumber()))
                .build();

        return JobParametersContext.builder()
                .application(csApplication)
                .jenkinsJobParameters(jenkinsJobParameters)
                .params(jenkinsJobParamsMap.getParams())
                .csOssBucket(csOssBucket)
                .jobName(Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey()))
                .build();
    }

    @Override
    public String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        // iOS Java Python
        // /应用名/任务名/任务编号/
        CsApplication csApplication = queryApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber = String.valueOf(jobBuild.getJobBuildNumber());
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csJobBuildArtifact.getArtifactFileName());
    }

    private BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCiJob csJob) {
        return TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.BUILD.getType()).acqJobEngine(csJob.getId());
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
