package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.*;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.decorator.jenkins.context.JobBuildContext;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.BuildArtifactVO;
import com.baiyi.caesar.domain.vo.build.BuildExecutorVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.*;
import com.baiyi.caesar.service.server.OcServerService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.NOT_EXTEND;

/**
 * @Author baiyi
 * @Date 2021/1/14 10:21 上午
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

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private JobTplDecorator jobTplDecorator;

    public List<CiJobBuildVO.JobBuild> decorator(List<CsCiJobBuild> jobBuilds, Integer extend) {
        JobBuildContext context = buildJobBuildContext(jobBuilds);
        return jobBuilds.stream().map(e ->
                decorator(e, context, extend)
        ).collect(Collectors.toList());
    }

    public CiJobBuildVO.JobBuild decorator(CsCiJobBuild jobBuild, Integer extend) {
        JobBuildContext context = buildJobBuildContext(Lists.newArrayList(jobBuild));
        return decorator(jobBuild, context, extend);
    }

    private JobBuildContext buildJobBuildContext(List<CsCiJobBuild> jobBuilds) {
        if (CollectionUtils.isEmpty(jobBuilds)) return JobBuildContext.builder().build();

        CsCiJobBuild jobBuild = jobBuilds.get(0);
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(jobBuild.getCiJobId());
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(csCiJob.getScmMemberId());
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(csApplicationScmMember.getScmId());
        JobTplVO.JobTpl jobTpl = new JobTplVO.JobTpl();
        if (!IDUtil.isEmpty(csCiJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(csCiJob.getJobTplId());
            jobTpl = jobTplDecorator.decorator(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class), NOT_EXTEND);
        }
        return JobBuildContext.builder()
                .csCiJob(csCiJob)
                .csOssBucket(ossBucketService.queryCsOssBucketById(csCiJob.getOssBucketId()))
                .jobEngineMap(acqJobEngineMap(jobBuilds))
                .csGitlabProject(csGitlabProject)
                .jobTpl(jobTpl)
                .build();

    }

    private Map<Integer, JobEngineVO.JobEngine> acqJobEngineMap(List<CsCiJobBuild> jobBuilds) {
        Map<Integer, JobEngineVO.JobEngine> jobEngineMap = Maps.newHashMap();
        jobBuilds.parallelStream().forEach(jobBuild -> {
            if (!jobEngineMap.containsKey(jobBuild.getJobEngineId())) {
                CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobBuild.getJobEngineId());
                if (csJobEngine != null) {
                    JobEngineVO.JobEngine jobEngine = jobEngineDecorator.decorator(BeanCopierUtil.copyProperties(csJobEngine, JobEngineVO.JobEngine.class));
                    jobEngineMap.put(jobBuild.getJobEngineId(), jobEngine);
                }
            }
        });
        return jobEngineMap;
    }

    private List<BuildArtifactVO.BuildArtifact> acqArtifacts(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        List<CsJobBuildArtifact> artifacts = csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(BuildType.BUILD.getType(), jobBuild.getId());
        if (!CollectionUtils.isEmpty(artifacts)) {
            return jobBuildArtifactDecorator.decorator(artifacts, context.getCsOssBucket());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public CiJobBuildVO.JobBuild decorator(CsCiJobBuild csCiJobBuild, JobBuildContext context, Integer extend) {
        CiJobBuildVO.JobBuild jobBuild = BeanCopierUtil.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class);
        // 操作用户
        jobBuild.setUser(acqUser(jobBuild));
        // Ago
        jobBuild.setAgo(acqAgo(jobBuild));
        if (extend == NOT_EXTEND) return jobBuild;
        // 组装构件
        jobBuild.setArtifacts(acqArtifacts(jobBuild, context));
        jobBuild.setNoArtifact(CollectionUtils.isEmpty(jobBuild.getArtifacts()));
        // 组装工作引擎
        jobBuild.setJobEngine(acqJobEngine(jobBuild, context));
        jobBuild.setJobBuildUrl(buildJobDetailUrl(jobBuild));
        // 组装变更记录
        jobBuild.setChanges(getChanges(jobBuild, context));
        // 组装构建时间
        jobBuild.setBuildTime(acqBuildTimes(jobBuild));
        jobBuild.setExecutors(getBuildExecutorByBuildId(jobBuild.getId()));
        // 组装Commit详情
        jobBuild.setCommitDetails(acqCommitDetails(jobBuild, context));
        // 支持回滚
        jobBuild.setSupportRollback(acqSupportRollback(context));

        return jobBuild;
    }

    private boolean acqSupportRollback(JobBuildContext context) {
        // 支持回滚
        return context.getJobTpl() != null && context.getJobTpl().getSupportRollback() != null && context.getJobTpl().getSupportRollback();

    }

    private CiJobBuildVO.BaseCommit acqCommitDetails(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        CiJobBuildVO.BaseCommit baseCommit = new CiJobBuildVO.BaseCommit();
        baseCommit.setCommit(jobBuild.getCommit());
        baseCommit.setCommitUrl(GitlabUtil.buildCommitUrl(context.getCsGitlabProject().getWebUrl(), jobBuild.getCommit()));
        return baseCommit;
    }


    private String acqBuildTimes(CiJobBuildVO.JobBuild jobBuild) {
        if (jobBuild.getStartTime() != null && jobBuild.getEndTime() != null) {
            long buildTime = jobBuild.getEndTime().getTime() - jobBuild.getStartTime().getTime();
            return TimeUtil.acqBuildTime(buildTime);
        }
        return "";
    }

    public List<BuildExecutorVO.BuildExecutor> getBuildExecutorByBuildId(int buildId) {
        List<CsJobBuildExecutor> executors = csJobBuildExecutorService.queryCsJobBuildExecutorByBuildId(BuildType.BUILD.getType(), buildId);
        return executors.stream().map(e -> {
                    BuildExecutorVO.BuildExecutor buildExecutor = BeanCopierUtil.copyProperties(e, BuildExecutorVO.BuildExecutor.class);
                    OcServer ocServer = ocServerService.queryOcServerByIp(buildExecutor.getPrivateIp());
                    if (ocServer != null)
                        buildExecutor.setServer(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
                    return buildExecutor;
                }
        ).collect(Collectors.toList());
    }

    private List<CiJobBuildVO.BuildChange> getBuildChanges(CsGitlabProject csGitlabProject, int buildId) {
        List<CsJobBuildChange> changes = csJobBuildChangeService.queryCsJobBuildChangeByBuildId(BuildType.BUILD.getType(), buildId);
        return changes.stream().map(e -> {
            CiJobBuildVO.BuildChange buildChange = BeanCopierUtil.copyProperties(e, CiJobBuildVO.BuildChange.class);
            buildChange.setShortCommitId(buildChange.getCommitId().substring(0, 7));

            buildChange.setCommitUrl(GitlabUtil.buildCommitUrl(csGitlabProject.getWebUrl(), buildChange.getCommitId()));
            return buildChange;
        }).collect(Collectors.toList());
    }

    private List<CiJobBuildVO.BuildChange> getChanges(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        return getBuildChanges(context.getCsGitlabProject(), jobBuild.getId());
    }

    /**
     * 获取工作引擎
     *
     * @param jobBuild
     * @param context
     * @return
     */
    private JobEngineVO.JobEngine acqJobEngine(CiJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        return context.getJobEngineMap().get(jobBuild.getJobEngineId());
    }

    /**
     * 装饰工作引擎
     * e.g:
     *  https://cc2.xinc818.com/blue/organizations/jenkins/CAESAR_caesar-server-build-prod/detail/CAESAR_caesar-server-build-prod/47/pipeline/
     * @param jobBuild
     */
    private String buildJobDetailUrl(CiJobBuildVO.JobBuild jobBuild) {
        return Joiner.on("/").skipNulls().join(jobBuild.getJobEngine().getJenkinsInstance().getUrl(),
                "blue/organizations/jenkins",
                jobBuild.getJobName(),
                "detail",
                jobBuild.getJobName(),
                jobBuild.getEngineBuildNumber(),
                "pipeline");
    }

    private String acqAgo(CiJobBuildVO.JobBuild jobBuild) {
        return TimeAgoUtil.format(jobBuild.getStartTime());
    }

    private UserVO.User acqUser(CiJobBuildVO.JobBuild jobBuild) {
        if (!StringUtils.isEmpty(jobBuild.getUsername())) {
            OcUser ocUser = ocUserService.queryOcUserByUsername(jobBuild.getUsername());
            if (ocUser != null) {
                ocUser.setPassword("");
                return BeanCopierUtil.copyProperties(ocUser, UserVO.User.class);
            }
        }
        return new UserVO.User();
    }


}
