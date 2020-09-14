package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.baiyi.caesar.common.base.AndroidReinforceChannelType;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.util.RegexUtils;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/27 5:13 下午
 * @Version 1.0
 */
@Slf4j
@Component("AndroidReinforceJobHandler")
public class AndroidReinforceJobHandler extends BaseDeploymentJobHandler implements IDeploymentJobHandler {

    @Override
    public String getKey() {
        return JobType.ANDROID_REINFORCE.getType();
    }

    private static final String CHANNEL_TYPE = "channelType";

    private static final String CHANNEL_GROUP = "channelGroup";

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCdJob, deploymentParam);
        JobParamUtils.invokeJobBuildNumberParam(csCdJob, jobParamDetail);
        JobParamUtils.invokeOssJobUrlParam(csCdJob, jobParamDetail);
        invokeChannelGroup(jobParamDetail, deploymentParam);
        return jobParamDetail;
    }

    @Override
    protected List<CsJobBuildArtifact> filterBuildArtifacts(List<CsJobBuildArtifact> artifacts) {
        return artifacts.stream().filter(e -> RegexUtils.checkApk(e.getArtifactFileName())).collect(Collectors.toList());
    }

    private void invokeChannelGroup(JobParamDetail jobParamDetail, JobDeploymentParam.DeploymentParam deploymentParam) {
        if (deploymentParam.getParamMap().get(CHANNEL_TYPE).equals("0")) return;
        if (!deploymentParam.getParamMap().containsKey(CHANNEL_GROUP)) return;
        Type type = new TypeToken<Set<String>>() {
        }.getType();
        Set<String> channelGroupSet = new GsonBuilder().create().fromJson(deploymentParam.getParamMap().get(CHANNEL_GROUP), type);

        String channelGroup = Joiner.on(" ").join(channelGroupSet.stream().map(this::buildApkName).collect(Collectors.toList()));
        jobParamDetail.getParams().put(CHANNEL_GROUP, channelGroup);
    }

    private String buildApkName(String channelType) {
        return Joiner.on("_").join("./sign/*", AndroidReinforceChannelType.getName(channelType), "sign.apk");
    }

}