package com.baiyi.caesar.factory.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:40 上午
 * @Version 1.0
 */
public interface IJenkinsJobHandler {

    BusinessWrapper<Boolean> build(CsCiJob csCiJob ,JobBuildParam.CiBuildParam buildParam);

    String getKey();

    String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsCiJobBuildArtifact csCiJobBuildArtifact);
}
