package com.baiyi.caesar.factory.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:40 上午
 * @Version 1.0
 */
public interface IJenkinsJobHandler {

    BusinessWrapper<Boolean> build(CsCiJob csCiJob ,JobBuildParam.CiBuildParam buildParam);

    String getKey();

    String acqOssPath(CsCiJobBuild csCiJobBuild, CsCiJobBuildArtifact csCiJobBuildArtifact);
}
