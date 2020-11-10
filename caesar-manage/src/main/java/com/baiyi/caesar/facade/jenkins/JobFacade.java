package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
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


    public BusinessWrapper<Boolean> buildCiJob(JobBuildParam.BuildParam buildParam);

    public BusinessWrapper<Boolean> abortCiJobBuild(int ciBuildId);

    public BusinessWrapper<Boolean> buildCdJob(JobDeploymentParam.DeploymentParam deploymentParam);

    public DataTable<CiJobBuildVO.JobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery);

    public DataTable<CdJobBuildVO.JobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery);

    public List<CiJobBuildVO.JobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query);

    public BusinessWrapper<String> viewJobBuildOutput(JobBuildParam.ViewJobBuildOutputQuery query);


    public CiJobBuildVO.JobBuild queryCiJobBuildByBuildId(@Valid int buildId);

    public CdJobBuildVO.JobBuild queryCdJobBuildByBuildId(@Valid int buildId);

    public BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> queryCdJobHostPatternByJobId(int cdJobId);

    /**
     * 校正构建Job引擎
     *
     * @param buildType
     * @param jobId
     * @return
     */
    public BusinessWrapper<Boolean> correctionJobEngine(int buildType, int jobId);


    public void trackJobBuildTask();

    public BusinessWrapper<Boolean> deleteBuildJob(int ciJobId);

    public BusinessWrapper<Boolean> deleteDeploymentJob(int cdJobId);


}
