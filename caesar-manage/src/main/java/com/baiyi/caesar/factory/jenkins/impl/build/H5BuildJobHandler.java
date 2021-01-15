package com.baiyi.caesar.factory.jenkins.impl.build;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/8/5 4:59 下午
 * @Version 1.0
 */
@Slf4j
@Component("H5CiJobHandler")
public class H5BuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Override
    public String getKey() {
        return JobType.HTML5.getType();
    }

    @Override
    public String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        // HTML5 /应用名/任务名/分支/
        CsApplication csApplication = queryApplicationById(jobBuild.getApplicationId());
        String applicationName = csApplication.getApplicationKey();
        String jobName = jobBuild.getJobName();
        String branch = jobBuild.getBranch();
        return Joiner.on("/").join(applicationName, jobName, branch ,csJobBuildArtifact.getArtifactFileName());
    }


}
