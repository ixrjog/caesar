package com.baiyi.caesar.factory.jenkins.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.factory.jenkins.IJenkinsJobHandler;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/8/5 4:59 下午
 * @Version 1.0
 */
@Slf4j
@Component("JenkinsH5JobHandler")
public class JenkinsH5JobHandler extends BaseJenkinsJobHandler implements IJenkinsJobHandler {

    @Override
    public String getKey() {
        return JobType.HTML5.getType();
    }

    @Override
    public String acqOssPath(CsCiJobBuild csCiJobBuild, CsCiJobBuildArtifact csCiJobBuildArtifact) {
        // HTML5
        // /应用名/任务名/分支/
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCiJobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = csCiJobBuild.getJobName();
        String branch = csCiJobBuild.getBranch();
        return Joiner.on("/").join(applicationName, jobName, branch ,csCiJobBuildArtifact.getArtifactFileName());
    }


}
