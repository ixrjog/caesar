package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.baiyi.caesar.common.base.AndroidReinforceChannelType;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/27 5:13 下午
 * @Version 1.0
 */
@Slf4j
@Component("AndroidReinforceCdJobHandler")
public class AndroidReinforceDeploymentJobHandler extends BaseDeploymentJobHandler implements IDeploymentJobHandler {

    @Override
    public String getKey() {
        return JobType.ANDROID_REINFORCE.getType();
    }

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCdJob, deploymentParam);

        if (deploymentParam.getParamMap().get("channelType").equals("0")) return jobParamDetail;
        if (!deploymentParam.getParamMap().containsKey("channelGroup")) return jobParamDetail;
        Type type = new TypeToken<Set<String>>() {}.getType();
        Set<String> channelGroupSet =  new GsonBuilder().create().fromJson(deploymentParam.getParamMap().get("channelGroup"),type);

        String channelGroup = Joiner.on(" ").join(channelGroupSet.stream().map(e->
                Joiner.on("_").join("./sign/*"  , AndroidReinforceChannelType.getName(e) , "sign.apk"
               )
        ).collect(Collectors.toList()));
        jobParamDetail.getParams().put("channelGroup", channelGroup);

        return jobParamDetail;
    }


    @Override
    public String acqOssPath(CdJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        // iOS Java Python
        // /应用名/任务名/任务编号/
        CsApplication csApplication = queryApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber = String.valueOf(jobBuild.getJobBuildNumber());
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csJobBuildArtifact.getArtifactFileName());
    }

}