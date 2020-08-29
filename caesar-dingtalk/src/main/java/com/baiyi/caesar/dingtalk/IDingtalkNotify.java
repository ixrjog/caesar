package com.baiyi.caesar.dingtalk;

import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.baiyi.caesar.jenkins.context.JobDeploymentContext;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:14 下午
 * @Version 1.0
 */
public interface IDingtalkNotify {

    String getKey();

    void doNotify(int noticePhase, JobBuildContext context);


    void doNotify(int noticePhase, JobDeploymentContext context);

}
