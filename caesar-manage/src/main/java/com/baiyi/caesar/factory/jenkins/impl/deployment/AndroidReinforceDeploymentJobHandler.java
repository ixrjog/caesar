package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public String acqOssPath(CdJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        // iOS Java Python
        // /应用名/任务名/任务编号/
        CsApplication csApplication = csApplicationService.queryCsApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber = String.valueOf(jobBuild.getJobBuildNumber());
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csJobBuildArtifact.getArtifactFileName());
    }

}