package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupHostPatternVO;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:51 下午
 * @Version 1.0
 */

public interface JobFacade {

    BusinessWrapper<Boolean> tryAuthorizedUser(CsCiJob csCiJob);

    BusinessWrapper<Boolean> buildCiJob(JobBuildParam.BuildParam buildParam);

    BusinessWrapper<Boolean> abortCiJobBuild(int ciBuildId);

    BusinessWrapper<Boolean> buildCdJob(JobDeploymentParam.DeploymentParam deploymentParam);

    DataTable<CiJobBuildVO.JobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery);

    DataTable<CdJobBuildVO.JobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery);

    List<CiJobBuildVO.JobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query);

    BusinessWrapper<String> viewJobBuildOutput(JobBuildParam.ViewJobBuildOutputQuery query);

    CiJobBuildVO.JobBuild queryCiJobBuildByBuildId(@Valid int buildId);

    CdJobBuildVO.JobBuild queryCdJobBuildByBuildId(@Valid int buildId);

    BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> queryCdJobHostPatternByJobId(int cdJobId);

    /**
     * 校正构建Job引擎
     *
     * @param buildType
     * @param jobId
     * @return
     */
    BusinessWrapper<Boolean> correctionJobEngine(int buildType, int jobId);

    void trackJobBuildTask();

    BusinessWrapper<Boolean> deleteBuildJob(int ciJobId);

    BusinessWrapper<Boolean> deleteDeploymentJob(int cdJobId);


}
