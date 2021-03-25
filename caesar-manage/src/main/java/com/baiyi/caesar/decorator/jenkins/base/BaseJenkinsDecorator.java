package com.baiyi.caesar.decorator.jenkins.base;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.common.util.TimeAgoUtil;
import com.baiyi.caesar.decorator.jenkins.context.JobBuildContext;
import com.baiyi.caesar.decorator.jenkins.util.BuildTimeUtil;
import com.baiyi.caesar.decorator.jenkins.util.JenkinsUtil;
import com.baiyi.caesar.decorator.user.UserDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildServer;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.base.AgoVO;
import com.baiyi.caesar.domain.vo.base.BuildTimeVO;
import com.baiyi.caesar.domain.vo.base.JobBuildVO;
import com.baiyi.caesar.domain.vo.build.*;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.jenkins.CsJobBuildArtifactService;
import com.baiyi.caesar.service.jenkins.CsJobBuildChangeService;
import com.baiyi.caesar.service.jenkins.CsJobBuildExecutorService;
import com.baiyi.caesar.service.jenkins.CsJobBuildServerService;
import com.baiyi.caesar.service.server.OcServerService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.caesar.decorator.base.BaseDecorator.NOT_EXTEND;

/**
 * @Author baiyi
 * @Date 2021/3/25 1:58 下午
 * @Version 1.0
 */
@Component
public class BaseJenkinsDecorator<T> {

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private UserDecorator userDecorator;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private CsJobBuildServerService csJobBuildServerService;

    protected  void decoratorDeploymentServers(CdJobBuildVO.IDeploymentServers iDeploymentServers) {
        List<CsJobBuildServer> csJobBuildServers = csJobBuildServerService.queryCsJobBuildServerByBuildId(BuildType.DEPLOYMENT.getType(), iDeploymentServers.getBuildId());
        iDeploymentServers.setServers(BeanCopierUtil.copyListProperties(csJobBuildServers, DeploymentServerVO.BuildServer.class));
    }


    protected void decoratorBuildExecutors(BuildExecutorVO.IBuildExecutors iBuildExecutors) {
        List<BuildExecutorVO.BuildExecutor> executors = csJobBuildExecutorService.queryCsJobBuildExecutorByBuildId(iBuildExecutors.getBuildType(), iBuildExecutors.getBuildId())
                .stream().map(e -> {
                            BuildExecutorVO.BuildExecutor buildExecutor = BeanCopierUtil.copyProperties(e, BuildExecutorVO.BuildExecutor.class);
                            OcServer ocServer = ocServerService.queryOcServerByIp(buildExecutor.getPrivateIp());
                            if (ocServer != null)
                                buildExecutor.setServer(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
                            return buildExecutor;
                        }
                ).collect(Collectors.toList());
        iBuildExecutors.setExecutors(executors);
    }


    protected void decoratorBuildChanges(CiJobBuildVO.IBuildChanges iBuildChanges, JobBuildContext context) {
        List<CiJobBuildVO.BuildChange> changes = csJobBuildChangeService.queryCsJobBuildChangeByBuildId(BuildType.BUILD.getType(), iBuildChanges.getBuildId())
                .stream().map(e -> {
                    CiJobBuildVO.BuildChange buildChange = BeanCopierUtil.copyProperties(e, CiJobBuildVO.BuildChange.class);
                    buildChange.setShortCommitId(buildChange.getCommitId().substring(0, 7));

                    buildChange.setCommitUrl(GitlabUtil.buildCommitUrl(context.getCsGitlabProject().getWebUrl(), buildChange.getCommitId()));
                    return buildChange;
                }).collect(Collectors.toList());
        iBuildChanges.setChanges(changes);
    }

    /**
     * 装饰任务详情Url
     * e.g:
     * https://cc2.xinc818.com/blue/organizations/jenkins/CAESAR_caesar-server-build-prod/detail/CAESAR_caesar-server-build-prod/47/pipeline/
     *
     * @param t
     */
    protected void decoratorJobDetailUrl(T t) {
        if (!(t instanceof JobEngineVO.IJobEngine)) return;
        if (!(t instanceof JobBuildVO.iJobBuild)) return;
        JobEngineVO.IJobEngine iJobEngine = (JobEngineVO.IJobEngine) t;
        JobBuildVO.iJobBuild iJobBuild = (JobBuildVO.iJobBuild) t;

        String url = Joiner.on("/").skipNulls().join(iJobEngine.getJobEngine().getJenkinsInstance().getUrl(),
                "blue/organizations/jenkins",
                iJobBuild.getJobName(),
                "detail",
                iJobBuild.getJobName(),
                iJobBuild.getEngineBuildNumber(),
                "pipeline");
        iJobBuild.setJobBuildUrl(url);
    }

    protected void decoratorBuildArtifacts(BuildArtifactVO.IBuildArtifacts iBuildArtifacts, JobBuildContext context) {
        List<CsJobBuildArtifact> artifacts = csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(iBuildArtifacts.getBuildType(), iBuildArtifacts.getBuildId());
        if (!CollectionUtils.isEmpty(artifacts)) {
            iBuildArtifacts.setArtifacts(JenkinsUtil.decoratorBuildArtifacts(artifacts, context.getCsOssBucket()));
            iBuildArtifacts.setNoArtifact(false);
        } else {
            iBuildArtifacts.setArtifacts(Collections.EMPTY_LIST);
            iBuildArtifacts.setNoArtifact(true);
        }
    }

    /**
     * 装饰工作引擎
     *
     * @param iJobEngine
     * @param context
     */
    protected void decoratorJobEngine(JobEngineVO.IJobEngine iJobEngine, JobBuildContext context) {
        iJobEngine.setJobEngine(context.getJobEngineMap().get(iJobEngine.getJobEngineId()));
    }


    protected void decoratorJobBuild(T t, Integer extend) {
        if (t instanceof UserVO.IUser)
            userDecorator.decorator((UserVO.IUser) t);

        if (t instanceof AgoVO.IAgo)
            TimeAgoUtil.decorator((AgoVO.IAgo) t);

        if (NOT_EXTEND == extend) return;
        if (t instanceof BuildTimeVO.IBuildTime)
            BuildTimeUtil.decorator((BuildTimeVO.IBuildTime) t);
    }
}
