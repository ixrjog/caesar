package com.baiyi.caesar.facade.jenkins.impl;


import com.baiyi.caesar.common.base.BuildOutputType;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.JenkinsUtils;
import com.baiyi.caesar.common.util.RedisKeyUtils;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
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
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.factory.jenkins.engine.JobEngineHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.*;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:51 下午
 * @Version 1.0
 */
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
    private JobBuildDecorator jobBuildDecorator;

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
    private JobEngineHandler jobEngineHandler;

    @Resource
    private JenkinsJobFacade jenkinsJobFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Override
    public BusinessWrapper<Boolean> buildCiJob(JobBuildParam.BuildParam buildParam) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById((buildParam.getCiJobId()));
        IBuildJobHandler iBuildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(csCiJob.getJobType());
        if (StringUtils.isEmpty(buildParam.getBranch()))
            buildParam.setBranch(csCiJob.getBranch());
        iBuildJobHandler.build(csCiJob, buildParam);
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
        redisUtil.set(RedisKeyUtils.getJobBuildAbortKey(ciBuildId), operationUser.getUsername(), 600);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> buildCdJob(JobDeploymentParam.DeploymentParam deploymentParam) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById((deploymentParam.getCdJobId()));
        IDeploymentJobHandler iDeploymentJobHandler = DeploymentJobHandlerFactory.getDeploymentJobByKey(csCdJob.getJobType());
        iDeploymentJobHandler.deployment(csCdJob, deploymentParam);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<CiJobBuildVO.JobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery) {
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(pageQuery);
        List<CiJobBuildVO.JobBuild> page = BeanCopierUtils.copyListProperties(table.getData(), CiJobBuildVO.JobBuild.class);
        return new DataTable<>(page.stream().map(e -> jobBuildDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<CdJobBuildVO.JobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery) {
        DataTable<CsCdJobBuild> table = csCdJobBuildService.queryCdJobBuildPage(pageQuery);
        List<CdJobBuildVO.JobBuild> page = BeanCopierUtils.copyListProperties(table.getData(), CdJobBuildVO.JobBuild.class);
        return new DataTable<>(page.stream().map(e -> jobDeploymentDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public List<CiJobBuildVO.JobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query) {
        if (query.getSize() == null)
            query.setSize(10);
        return csCiJobBuildService.queryCiJobBuildArtifact(query)
                .stream().map(e -> jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(e, CiJobBuildVO.JobBuild.class), 1)).collect(Collectors.toList());
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
    public CiJobBuildVO.JobBuild queryCiJobBuildByBuildId(@Valid int buildId) {
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(buildId);
        return jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class), 1);
    }

    @Override
    public CdJobBuildVO.JobBuild queryCdJobBuildByBuildId(@Valid int buildId) {
        CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(buildId);
        return jobDeploymentDecorator.decorator(BeanCopierUtils.copyProperties(csCdJobBuild, CdJobBuildVO.JobBuild.class), 1);
    }

    @Override
    public BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> queryCdJobHostPatternByJobId(int cdJobId) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(cdJobId);
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtils.convert(csCdJob.getParameterYaml());
        Map<String, String> paramMap = JenkinsUtils.convert(jenkinsJobParameters);
        if (!paramMap.containsKey("serverGroup"))
            return new BusinessWrapper(ErrorEnum.JENKINS_JOB_TPL_HOST_PATTERN_IS_NOT_CONFIGURED);
        String serverGroupName = paramMap.get("serverGroup");
        List<ApplicationServerGroupVO.ApplicationServerGroup> serverGroups = applicationFacade.queryApplicationServerGroupByApplicationId(csCdJob.getApplicationId());
        // 鉴权（必须在应用中指定服务器组配置）
        if (serverGroups.stream().noneMatch(e -> e.getServerGroupName().equals(serverGroupName)))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SERVERGROUP_NON_COMPLIANCE);
        return serverGroupFacade.queryServerGroupHostPattern(serverGroupName);
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
        List<CsJobEngine> csJobEngines = jobEngineHandler.queryJobEngine(buildType, jobId);
        if (!CollectionUtils.isEmpty(csJobEngines))
            for (CsJobEngine csJobEngine : csJobEngines) {
                CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(csJobEngine.getJenkinsInstanceId());
                if (jenkinsServerHandler.isActive(csJenkinsInstance.getName())) {
                    try {
                        JobWithDetails job = jenkinsServerHandler.getJob(csJenkinsInstance.getName(), csJobEngine.getName());
                        if (job.getLastBuild() == null) {
                            saveCsJobEngine(csJobEngine, 0);
                        } else {
                            saveCsJobEngine(csJobEngine, job.getLastBuild().getNumber());
                        }
                    } catch (Exception ex) {
                        return new BusinessWrapper<>(ErrorEnum.JENKINS_CORRECTION_JOB_ENGINE);
                    }
                }
            }
        return BusinessWrapper.SUCCESS;
    }

    private void saveCsJobEngine(CsJobEngine csJobEngine, int lastBuildNumber) {
        if (csJobEngine.getLastBuildNumber() == lastBuildNumber && csJobEngine.getNextBuildNumber() == lastBuildNumber + 1) {
            csJobEngine.setLastBuildNumber(lastBuildNumber);
            csJobEngine.setNextBuildNumber(lastBuildNumber + 1);
        }
    }

    @Override
    public void trackJobBuildTask() {
        List<CsCiJobBuild> csCiJobBuilds = csCiJobBuildService.queryCsCiJobBuildByFinalized(false);
        if (CollectionUtils.isEmpty(csCiJobBuilds)) return;
        csCiJobBuilds.forEach(e -> {
            String key = RedisKeyUtils.getJobBuildKey(e.getId());
            if (!redisUtil.hasKey(key)) {
                CsCiJob csCiJob = csCiJobService.queryCsCiJobById((e.getCiJobId()));
                IBuildJobHandler jenkinsJobHandler = BuildJobHandlerFactory.getBuildJobByKey(csCiJob.getJobType());
                jenkinsJobHandler.trackJobBuild(e);
            }
        });
    }

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
                if(!deleteBuildDetails(BuildType.BUILD.getType(), csCdJobBuild.getId()))
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
                    .forEach(e -> csJobBuildExecutorService.deleteCsJobBuildExecutorById(e.getId()));
            // server
            csJobBuildServerService.queryCsJobBuildServerByBuildId(buildType, buildId)
                    .forEach(e -> csJobBuildServerService.deleteCsJobBuildServerById(e.getId()));
            // change
            csJobBuildChangeService.queryCsJobBuildChangeByBuildId(buildType, buildId)
                    .forEach(e -> csJobBuildChangeService.deleteCsJobBuildChangeById(e.getId()));
            // artifact
            csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(buildType, buildId)
                    .forEach(e -> csJobBuildArtifactService.deleteCsJobBuildArtifactById(e.getId()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}