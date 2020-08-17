package com.baiyi.caesar.dingtalk;

import com.baiyi.caesar.jenkins.context.JobBuildContext;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:14 下午
 * @Version 1.0
 */
public interface IDingtalkNotify {

    String getKey();

    void doNotify(int noticeType,int noticePhase, JobBuildContext jobBuildContext );

}
