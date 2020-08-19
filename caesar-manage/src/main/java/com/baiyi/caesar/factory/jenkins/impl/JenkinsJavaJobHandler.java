package com.baiyi.caesar.factory.jenkins.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.IJenkinsJobHandler;
import com.baiyi.caesar.factory.jenkins.model.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/8/18 3:54 下午
 * @Version 1.0
 */
@Slf4j
@Component("JenkinsJavaJobHandler")
public class JenkinsJavaJobHandler extends BaseJenkinsJobHandler implements IJenkinsJobHandler {

    @Override
    public String getKey() {
        return JobType.JAVA.getType();
    }

    @Override
    public String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsCiJobBuildArtifact csCiJobBuildArtifact) {
        // Java
        // /应用名/任务名/任务编号/
        CsApplication csApplication = csApplicationService.queryCsApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String jobBuildNumber =String.valueOf(jobBuild.getJobBuildNumber()) ;
        return Joiner.on("/").join(applicationName, jobName, jobBuildNumber, csCiJobBuildArtifact.getArtifactFileName());
    }

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCiJob csCiJob, JobBuildParam.CiBuildParam buildParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCiJob, buildParam);
        JobParamUtils.invokeJobBuildNumberParam(csCiJob, jobParamDetail);
        return jobParamDetail;
    }


}
