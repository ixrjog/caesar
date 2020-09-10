package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.util.RegexUtils;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/9/10 4:54 下午
 * @Version 1.0
 */
@Slf4j
@Component("JavaDeployJobHandler")
public class JavaDeployJobHandler extends BaseDeploymentJobHandler implements IDeploymentJobHandler {

    @Override
    public String getKey() {
        return JobType.JAVA_DEPLOYMENT.getType();
    }

    private static final String HOST_PATTERN = "hostPattern";

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCdJob, deploymentParam);
        JobParamUtils.invokeJobBuildNumberParam(csCdJob, jobParamDetail);
        JobParamUtils.invokeOssJobUrlParam(csCdJob, jobParamDetail);
        invokeOssPath(jobParamDetail, deploymentParam);
        JobParamUtils.invokeHostPatternParam(jobParamDetail, deploymentParam);
        return jobParamDetail;
    }

    @Override
    protected List<CsJobBuildArtifact> filterBuildArtifacts(List<CsJobBuildArtifact> artifacts) {
        return artifacts.stream().filter(e -> RegexUtils.checkApk(e.getArtifactFileName())).collect(Collectors.toList());
    }

    private void invokeOssPath(JobParamDetail jobParamDetail, JobDeploymentParam.DeploymentParam deploymentParam) {
        List<CsJobBuildArtifact> artifacts = acqBuildArtifacts(deploymentParam.getCiBuildId());
        if (!CollectionUtils.isEmpty(artifacts))
            jobParamDetail.getParams().put("ossPath", artifacts.get(0).getStoragePath());
    }


}