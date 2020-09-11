package com.baiyi.caesar.dingtalk;

import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:14 下午
 * @Version 1.0
 */
public interface IDingtalkNotify {

    String getKey();

    void doNotify(int noticePhase, BuildJobContext context);

    void doNotify(int noticePhase, DeploymentJobContext context);

}
