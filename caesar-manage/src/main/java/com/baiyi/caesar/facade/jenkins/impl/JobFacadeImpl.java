package com.baiyi.caesar.facade.jenkins.impl;


import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.base.BuildOutputType;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.JenkinsUtil;
import com.baiyi.caesar.common.util.RedisKeyUtil;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.application.ApplicationServerGroupVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupHostPatternVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.ServerGroupFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsJobFacade;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import com.baiyi.caesar.facade.jenkins.factory.IJobEngine;
import com.baiyi.caesar.facade.jenkins.factory.JobEngineFactory;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.*;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.*;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:51 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JobFacadeImpl implements JobFacade {

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobBuildDecorator jobBuildsDecorator;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JobDeploymentDecorator jobDeploymentDecorator;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private CsJobBuildServerService csJobBuildServerService;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private UserFacade userFacade;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JenkinsJobFacade jenkinsJobFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Override
    public BusinessWrapper<Boolean> buildCiJob(JobBuildParam.BuildParam buildParam) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById((buildParam.getCiJobId()));
        // 鉴权
        BusinessWrapper<Boolean> tryAuthorizedUserWrapper = tryAuthorizedUser(csCiJob);
        if (!tryAuthorizedUserWrapper.isSuccess())
            return tryAuthorizedUserWrapper;
        correctionJobEngine(BuildType.BUILD.getType(), csCiJob.getId());    // 校正引擎

        BuildJobHandlerFactory.getBuildJobByKey(csCiJob.getJobType())
                .build(csCiJob, buildParam);
        return BusinessWrapper.SUCCESS;
    }


    @Override
    public BusinessWrapper<Boolean> tryAuthorizedUser(CsCiJob csCiJob) {
        OcUser operationUser = userFacade.getOcUserBySession();
        // 允许运维操作
        BusinessWrapper<Boolean> checkAccessLevelWrapper = userPermissionFacade.checkAccessLevel(operationUser, AccessLevel.OPS.getLevel());
        if (checkAccessLevelWrapper.isSuccess())
            return checkAccessLevelWrapper;

        // 应用管理员
        OcUserPermission ocUserPermission
                = userPermissionFacade.queryUserPermissionByUniqueKey(operationUser.getId(), BusinessType.APPLICATION.getType(), csCiJob.getApplicationId());
        if (ocUserPermission != null && "ADMIN" .equalsIgnoreCase(ocUserPermission.getRoleName()))
            return BusinessWrapper.SUCCESS;

        List<OcUserPermission> permissions = userPermissionFacade.queryBusinessPermission(BusinessType.APPLICATION_BUILD_JOB.getType(), csCiJob.getId());
        // 判断是否有任务授权
        if (CollectionUtils.isEmpty(permissions)) {
            // 鉴权到应用
            if (!userPermissionFacade.tryUserBusinessPermission(operationUser.getId(), BusinessType.APPLICATION.getType(), csCiJob.getApplicationId()))
                return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        } else {
            // 鉴权到job
            if (permissions.stream().noneMatch(e -> e.getUserId().equals(operationUser.getId())))
                return new BusinessWrapper<>(ErrorEnum.APPLICATION_JOB_AUTHENTICATION_FAILUER_2);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> abortCiJobBuild(int ciBuildId) {
        OcUser operationUser = userFacade.getOcUserBySession();
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(ciBuildId);
        if (!csCiJobBuild.getUsername().equals(operationUser.getUsername())) {
            if (!applicationFacade.isApplicationAdmin(csCiJobBuild.getApplicationId(), operationUser.getId()))
                return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        }
        redisUtil.set(RedisKeyUtil.getJobBuildAbortKey(ciBuildId), operationUser.getUsername(), 600);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> buildCdJob(JobDeploymentParam.DeploymentParam deploymentParam) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById((deploymentParam.getCdJobId()));
        correctionJobEngine(BuildType.DEPLOYMENT.getType(), csCdJob.getId());    // 校正引擎
        DeploymentJobHandlerFactory.getDeploymentJobByKey(csCdJob.getJobType()).deployment(csCdJob, deploymentParam);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<CiJobBuildVO.JobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery) {
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(pageQuery);
        return new DataTable<>(jobBuildsDecorator.decorator(table.getData(), pageQuery.getExtend()), table.getTotalNum());
    }

    @Override
    public DataTable<CdJobBuildVO.JobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery) {
        DataTable<CsCdJobBuild> table = csCdJobBuildService.queryCdJobBuildPage(pageQuery);
        return new DataTable<>(table.getData().stream().map(e ->
                jobDeploymentDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public List<CiJobBuildVO.JobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query) {
        if (query.getSize() == null)
            query.setSize(10);
        return jobBuildsDecorator.decorator(csCiJobBuildService.queryCiJobBuildArtifact(query), EXTEND);
    }

    @Override
    public BusinessWrapper<String> viewJobBuildOutput(JobBuildParam.ViewJobBuildOutputQuery query) {
        if (query.getBuildType() == BuildType.BUILD.getType()) {
            CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(query.getBuildId());
            return viewJobBuildOutput(csCiJobBuild.getJobEngineId(), csCiJobBuild.getJobName(), csCiJobBuild.getEngineBuildNumber());
        } else {
            CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(query.getBuildId());
            return viewJobBuildOutput(csCdJobBuild.getJobEngineId(), csCdJobBuild.getJobName(), csCdJobBuild.getEngineBuildNumber());
        }
    }

    @Override
    public void buildOutput(int buildType, int buildId, Session session) {
        if (buildType == BuildType.BUILD.getType()) {
            CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(buildId);
            jobBuildOutputTask(csCiJobBuild.getJobEngineId(), csCiJobBuild.getJobName(), csCiJobBuild.getEngineBuildNumber(), session);
        } else {
            CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(buildId);
            jobBuildOutputTask(csCdJobBuild.getJobEngineId(), csCdJobBuild.getJobName(), csCdJobBuild.getEngineBuildNumber(), session);
        }
    }

    private void jobBuildOutputTask(int jobEngineId, String jobName, int buildNumber, Session session) {
        CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobEngineId);
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(csJobEngine.getJenkinsInstanceId());
        JobWithDetails job = jenkinsServerHandler.getJob(csJenkinsInstance.getName(), jobName);
        Build build = job.getBuildByNumber(buildNumber);
        try {
            BuildWithDetails buildWithDetails = build.details();
            jenkinsServerHandler.streamConsoleOutput(buildWithDetails, session);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
            log.error("error {}", ie.getMessage());
            ie.printStackTrace();
        }

    }


    private BusinessWrapper<String> viewJobBuildOutput(int jobEngineId, String jobName, int buildNumber) {
        CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobEngineId);
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(csJobEngine.getJenkinsInstanceId());
        JobWithDetails job = jenkinsServerHandler.getJob(csJenkinsInstance.getName(), jobName);
        try {
            return new BusinessWrapper(jenkinsServerHandler.getBuildOutputByType(job, buildNumber, BuildOutputType.TEXT.getType()));
        } catch (IOException e) {
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_BUILD_OUTPUT_NOT_EXIST);
        }
    }

    @Override
    public CiJobBuildVO.JobBuild queryCiJobBuildByBuildId(int buildId) {
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(buildId);
        return jobBuildsDecorator.decorator(csCiJobBuild, EXTEND);
    }

    @Override
    public CdJobBuildVO.JobBuild queryCdJobBuildByBuildId(int buildId) {
        CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(buildId);
        return jobDeploymentDecorator.decorator(csCdJobBuild, EXTEND);
    }

    @Override
    public BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> queryCdJobHostPatternByJobId(int cdJobId) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(cdJobId);
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(csCdJob.getCiJobId());
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtil.convert(csCdJob.getParameterYaml());
        Map<String, String> paramMap = JenkinsUtil.convert(jenkinsJobParameters);
        if (!paramMap.containsKey(SERVER_GRUOP))
            return new BusinessWrapper(ErrorEnum.JENKINS_JOB_TPL_HOST_PATTERN_IS_NOT_CONFIGURED);
        String serverGroupName = paramMap.get(SERVER_GRUOP);
        List<ApplicationServerGroupVO.ApplicationServerGroup> serverGroups = applicationFacade.queryApplicationServerGroupByApplicationId(csCdJob.getApplicationId());
        // 鉴权（必须在应用中指定服务器组配置）
        if (serverGroups.stream().noneMatch(e -> e.getServerGroupName().equals(serverGroupName)))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SERVERGROUP_NON_COMPLIANCE);

        BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> hostPatternWrapper
                = serverGroupFacade.queryServerGroupHostPattern(serverGroupName, csCiJob.getEnvType());
        if (!hostPatternWrapper.isSuccess())
            return hostPatternWrapper;

        if (paramMap.containsKey(HOST_PATTERN)) {
            String hostPattern = paramMap.get(HOST_PATTERN);
            try {
                // 默认选中主机分组
                ServerGroupHostPatternVO.HostPattern hp = acqHostPattern(hostPatternWrapper.getBody(), hostPattern);
            } catch (NullPointerException e) {

            }
        }
        return serverGroupFacade.queryServerGroupHostPattern(serverGroupName, csCiJob.getEnvType());
    }

    private ServerGroupHostPatternVO.HostPattern acqHostPattern(List<ServerGroupHostPatternVO.HostPattern> hostPatterns, String hostPattern) throws NullPointerException {
        for (ServerGroupHostPatternVO.HostPattern h : hostPatterns) {
            if (h.getHostPattern().equals(hostPattern))
                return h;
        }
        throw new NullPointerException();
    }

    /**
     * 校正构建Job引擎
     *
     * @param buildType
     * @param jobId
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> correctionJobEngine(int buildType, int jobId) {
        IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(buildType);
        return iJobEngine.correctionJobEngine(jobId);
    }

    @Override
    public void trackJobBuildTask() {
        List<CsCiJobBuild> csCiJobBuilds = csCiJobBuildService.queryCsCiJobBuildByFinalized(false);
        if (CollectionUtils.isEmpty(csCiJobBuilds)) return;
        csCiJobBuilds.forEach(e -> {
            String key = RedisKeyUtil.getJobBuildKey(e.getId());
            if (!redisUtil.hasKey(key)) {
                CsCiJob csCiJob = csCiJobService.queryCsCiJobById((e.getCiJobId()));
                IBuildJobHandler jenkinsJobHandler = BuildJobHandlerFactory.getBuildJobByKey(csCiJob.getJobType());
                jenkinsJobHandler.trackJobBuild(e);
            }
        });
    }

    @Override
    public BusinessWrapper<Boolean> deleteBuildJob(int ciJobId) {
        // 删除 build
        List<CsCiJobBuild> builds = csCiJobBuildService.queryCiJobBuildByCiJobId(ciJobId);
        if (!CollectionUtils.isEmpty(builds)) {
            for (CsCiJobBuild csCiJobBuild : builds) {
                if (!deleteBuildDetails(BuildType.BUILD.getType(), csCiJobBuild.getId()))
                    return new BusinessWrapper<>(ErrorEnum.JENKINS_DELETE_JOB_BUILD_DETAILS_ERROR);
                csCiJobBuildService.deleteCsCiJobBuildById(csCiJobBuild.getId());
            }
            if (!jenkinsJobFacade.deleteJobBuildEngine(ciJobId))
                return new BusinessWrapper<>(ErrorEnum.JENKINS_DELETE_JOB_ENGINE_ERROR);
        }
        // 清理job授权
        userPermissionFacade.cleanBusinessPermission(BusinessType.APPLICATION_BUILD_JOB.getType(), ciJobId);
        // 删除任务
        csCiJobService.deleteCsCiJobById(ciJobId);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteDeploymentJob(int cdJobId) {
        // 删除 build
        List<CsCdJobBuild> builds = csCdJobBuildService.queryCdJobBuildByCdJobId(cdJobId);
        if (!CollectionUtils.isEmpty(builds)) {
            for (CsCdJobBuild csCdJobBuild : builds) {
                if (!deleteBuildDetails(BuildType.BUILD.getType(), csCdJobBuild.getId()))
                    return new BusinessWrapper<>(ErrorEnum.JENKINS_DELETE_JOB_BUILD_DETAILS_ERROR);
                csCdJobBuildService.deleteCsCdJobBuildById(csCdJobBuild.getId());
            }
            jenkinsJobFacade.deleteJobDeploymentEngine(cdJobId);
        }
        // 清理job授权
        userPermissionFacade.cleanBusinessPermission(BusinessType.APPLICATION_DEPLOYMENT_JOB.getType(), cdJobId);
        // 删除任务
        csCdJobService.deleteCsCdJobById(cdJobId);
        return BusinessWrapper.SUCCESS;
    }

    private boolean deleteBuildDetails(int buildType, int buildId) {
        try {
            // executor
            csJobBuildExecutorService.queryCsJobBuildExecutorByBuildId(buildType, buildId)
                    .parallelStream().forEach(e -> csJobBuildExecutorService.deleteCsJobBuildExecutorById(e.getId()));
            // server
            csJobBuildServerService.queryCsJobBuildServerByBuildId(buildType, buildId)
                    .parallelStream().forEach(e -> csJobBuildServerService.deleteCsJobBuildServerById(e.getId()));
            // change
            csJobBuildChangeService.queryCsJobBuildChangeByBuildId(buildType, buildId)
                    .parallelStream().forEach(e -> csJobBuildChangeService.deleteCsJobBuildChangeById(e.getId()));
            // artifact
            csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(buildType, buildId)
                    .parallelStream().forEach(e -> csJobBuildArtifactService.deleteCsJobBuildArtifactById(e.getId()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
