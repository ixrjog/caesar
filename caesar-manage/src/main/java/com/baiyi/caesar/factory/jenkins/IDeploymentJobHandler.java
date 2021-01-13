package com.baiyi.caesar.factory.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/27 4:46 下午
 * @Version 1.0
 */
public interface IDeploymentJobHandler {

    BusinessWrapper<Boolean> deployment(CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam);

    List<CsJobBuildArtifact> acqBuildArtifacts(int buildId);

    void trackJobDeployment(CsCdJobBuild csCdJobBuild);

    DeploymentJobContext acqDeploymentJobContext(CsCdJobBuild csCdJobBuild);

    String getKey();

    String acqOssPath(CdJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact);
}
