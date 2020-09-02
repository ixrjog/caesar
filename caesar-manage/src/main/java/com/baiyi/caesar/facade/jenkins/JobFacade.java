package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.common.base.BuildOutputType;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.RedisKeyUtils;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
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
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:51 下午
 * @Version 1.0
 */
@Component
public class JobFacade {

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
    private RedisUtil redisUtil;

    public BusinessWrapper<Boolean> buildCiJob(JobBuildParam.BuildQuery buildParam) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById((buildParam.getCiJobId()));
        IBuildJobHandler iBuildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(csCiJob.getJobType());
        if (StringUtils.isEmpty(buildParam.getBranch()))
            buildParam.setBranch(csCiJob.getBranch());
        iBuildJobHandler.build(csCiJob, buildParam);
        return BusinessWrapper.SUCCESS;
    }

    public BusinessWrapper<Boolean> buildCdJob(JobDeploymentParam.DeploymentParam deploymentParam) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById((deploymentParam.getCdJobId()));
        IDeploymentJobHandler iDeploymentJobHandler = DeploymentJobHandlerFactory.getDeploymentJobByKey(csCdJob.getJobType());
        iDeploymentJobHandler.deployment(csCdJob, deploymentParam);
        return BusinessWrapper.SUCCESS;
    }

    public DataTable<CiJobBuildVO.JobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery) {
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(pageQuery);
        List<CiJobBuildVO.JobBuild> page = BeanCopierUtils.copyListProperties(table.getData(), CiJobBuildVO.JobBuild.class);
        return new DataTable<>(page.stream().map(e -> jobBuildDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }


    public DataTable<CdJobBuildVO.JobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery) {
        DataTable<CsCdJobBuild> table = csCdJobBuildService.queryCdJobBuildPage(pageQuery);
        List<CdJobBuildVO.JobBuild> page = BeanCopierUtils.copyListProperties(table.getData(), CdJobBuildVO.JobBuild.class);
        return new DataTable<>(page.stream().map(e -> jobDeploymentDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    public List<CiJobBuildVO.JobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query) {
        if (query.getSize() == null)
            query.setSize(10);
        return csCiJobBuildService.queryCiJobBuildArtifact(query)
                .stream().map(e -> jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(e, CiJobBuildVO.JobBuild.class), 1)).collect(Collectors.toList());
    }

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

    public CiJobBuildVO.JobBuild queryCiJobBuildByBuildId(@Valid int buildId) {
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(buildId);
        return jobBuildDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class), 1);
    }

    public CdJobBuildVO.JobBuild queryCdJobBuildByBuildId(@Valid int buildId) {
        CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(buildId);
        return jobDeploymentDecorator.decorator(BeanCopierUtils.copyProperties(csCdJobBuild, CdJobBuildVO.JobBuild.class), 1);
    }

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

}
