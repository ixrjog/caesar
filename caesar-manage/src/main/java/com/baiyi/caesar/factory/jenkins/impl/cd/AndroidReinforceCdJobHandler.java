package com.baiyi.caesar.factory.jenkins.impl.cd;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.ICdJobHandler;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
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
public class AndroidReinforceCdJobHandler extends BaseCdJobHandler implements ICdJobHandler {

    @Override
    public String getKey() {
        return JobType.ANDROID_REINFORCE.getType();
    }

    @Override
    public String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsCiJobBuildArtifact csCiJobBuildArtifact) {
        // iOS Java Python
        // /应用名/任务名/任务编号/
        CsApplication csApplication = csApplicationService.queryCsApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber = String.valueOf(jobBuild.getJobBuildNumber());
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csCiJobBuildArtifact.getArtifactFileName());
    }

}