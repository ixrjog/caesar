package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.GitlabUtils;
import com.baiyi.caesar.common.util.TimeAgoUtils;
import com.baiyi.caesar.common.util.TimeUtils;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.decorator.jenkins.context.JobBuildContext;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.BuildExecutorVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.*;
import com.baiyi.caesar.service.server.OcServerService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/10 10:13 上午
 * @Version 1.0
 */
@Component
public class JobBuildDecorator {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private JobBuildArtifactDecorator jobBuildArtifactDecorator;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsOssBucketService ossBucketService;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    public List<CiJobBuildVO.JobBuild> decorator(List<CsCiJobBuild> jobBuilds, Integer extend) {
        JobBuildContext context = JobBuildContext.builder().build();
        return jobBuilds.stream().map(e -> decorator(e, context, extend)).collect(Collectors.toList());
    }

    private CiJobBuildVO.JobBuild decorator(CsCiJobBuild csCiJobBuild, JobBuildContext context, Integer extend) {
        CiJobBuildVO.JobBuild jobBuild = BeanCopierUtils.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class);
        return decorator(jobBuild, context, extend);
    }

    /**
     * 装饰工作引擎
     *
     * @param jobBuild
     * @param context
     */
    private void encapsulationJobEngine(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        if (!context.getJobEngineMap().containsKey(jobBuild.getJobEngineId())) {
            CsJobEngine csCiJobEngine = csJobEngineService.queryCsJobEngineById(jobBuild.getJobEngineId());
            if (csCiJobEngine != null) {
                JobEngineVO.JobEngine jobEngine = jobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobEngine, JobEngineVO.JobEngine.class));
                context.getJobEngineMap().put(jobBuild.getJobEngineId(), jobEngine);
            }
        }

        if (context.getJobEngineMap().containsKey(jobBuild.getJobEngineId())) {
            JobEngineVO.JobEngine jobEngine = context.getJobEngineMap().get(jobBuild.getJobEngineId());
            jobBuild.setJobEngine(jobEngine);
            String jobBuildUrl = Joiner.on("/").join(jobEngine.getJobUrl(), jobBuild.getEngineBuildNumber());
            jobBuild.setJobBuildUrl(jobBuildUrl);
        }
    }

    private void encapsulationChanges(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        if (context.getCsGitlabProject() == null) {
            CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(context.getCsCiJob().getScmMemberId());
            CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(csApplicationScmMember.getScmId());
            context.setCsGitlabProject(csGitlabProject);
        }
        jobBuild.setChanges(getBuildChangeByBuildId(context.getCsGitlabProject(), jobBuild.getId()));
    }

    private void encapsulationArtifacts(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        if (context.getCsOssBucket() == null) {
            CsCiJob csCiJob = csCiJobService.queryCsCiJobById(jobBuild.getCiJobId());
            context.setCsCiJob(csCiJob);
            CsOssBucket csOssBucket = ossBucketService.queryCsOssBucketById(csCiJob.getOssBucketId());
            context.setCsOssBucket(csOssBucket);
        }
        List<CsJobBuildArtifact> artifacts = csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(BuildType.BUILD.getType(), jobBuild.getId());
        if (CollectionUtils.isEmpty(artifacts)) {
            jobBuild.setNoArtifact(true);
        } else {
            jobBuild.setArtifacts(jobBuildArtifactDecorator.decorator(artifacts, context.getCsOssBucket()));
            jobBuild.setNoArtifact(false);
        }
    }

    private void encapsulationBuildTimes(CiJobBuildVO.JobBuild jobBuild) {
        if (jobBuild.getStartTime() != null && jobBuild.getEndTime() != null) {
            long buildTime = jobBuild.getEndTime().getTime() - jobBuild.getStartTime().getTime();
            jobBuild.setBuildTime(TimeUtils.acqBuildTime(buildTime));
        }
    }

    private void encapsulationAgo(CiJobBuildVO.JobBuild jobBuild) {
        jobBuild.setAgo(TimeAgoUtils.format(jobBuild.getStartTime()));
        if (!StringUtils.isEmpty(jobBuild.getUsername())) {
            OcUser ocUser = ocUserService.queryOcUserByUsername(jobBuild.getUsername());
            if (ocUser != null) {
                ocUser.setPassword("");
                jobBuild.setUser(BeanCopierUtils.copyProperties(ocUser, UserVO.User.class));
            }
        }
    }

    public CiJobBuildVO.JobBuild decorator(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context, Integer extend) {
        // Ago
        encapsulationAgo(jobBuild);

        if (extend == 0) return jobBuild;
        // 组装工作引擎
        encapsulationJobEngine(jobBuild, context);
        // 组装构件
        encapsulationArtifacts(jobBuild, context);
        // 组装边更记录
        encapsulationChanges(jobBuild, context);
        // 组装构建时间
        encapsulationBuildTimes(jobBuild);
        jobBuild.setExecutors(getBuildExecutorByBuildId(jobBuild.getId()));
        // 组装Commit详情
        encapsulationCommitDetails(jobBuild, context);
        return jobBuild;
    }

    private void encapsulationCommitDetails(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        CiJobBuildVO.BaseCommit baseCommit = new CiJobBuildVO.BaseCommit();
        baseCommit.setCommit(jobBuild.getCommit());
        baseCommit.setCommitUrl(GitlabUtils.acqCommitUrl(context.getCsGitlabProject().getWebUrl(), jobBuild.getCommit()));
        jobBuild.setCommitDetails(baseCommit);

    }

    private List<CiJobBuildVO.BuildChange> getBuildChangeByBuildId(CsGitlabProject csGitlabProject, int buildId) {
        List<CsJobBuildChange> changes = csJobBuildChangeService.queryCsJobBuildChangeByBuildId(BuildType.BUILD.getType(), buildId);
        return changes.stream().map(e -> {
            CiJobBuildVO.BuildChange buildChange = BeanCopierUtils.copyProperties(e, CiJobBuildVO.BuildChange.class);
            buildChange.setShortCommitId(buildChange.getCommitId().substring(0, 7));

            buildChange.setCommitUrl(GitlabUtils.acqCommitUrl(csGitlabProject.getWebUrl(), buildChange.getCommitId()));
            return buildChange;
        }).collect(Collectors.toList());
    }

    public List<BuildExecutorVO.BuildExecutor> getBuildExecutorByBuildId(int buildId) {
        List<CsJobBuildExecutor> executors = csJobBuildExecutorService.queryCsJobBuildExecutorByBuildId(BuildType.BUILD.getType(), buildId);
        return executors.stream().map(e -> {
                    BuildExecutorVO.BuildExecutor buildExecutor = BeanCopierUtils.copyProperties(e, BuildExecutorVO.BuildExecutor.class);
                    OcServer ocServer = ocServerService.queryOcServerByIp(buildExecutor.getPrivateIp());
                    if (ocServer != null)
                        buildExecutor.setServer(BeanCopierUtils.copyProperties(ocServer, ServerVO.Server.class));
                    return buildExecutor;
                }
        ).collect(Collectors.toList());
    }

}
