package com.baiyi.caesar.factory.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:40 上午
 * @Version 1.0
 */
public interface IJenkinsJobHandler {

    BusinessWrapper<Boolean> build(JobBuildParam.CiJobBuild ciJobBuild);

    String getKey();
}
