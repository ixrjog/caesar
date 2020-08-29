package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/18 5:33 下午
 * @Version 1.0
 */
@Slf4j
@Component("DingtalkJavaNotify")
public class JavaNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobType.JAVA.getType();
    }

    @Override
    protected Map<String, Object> acqTemplateContent(int noticeType, int noticePhase, JobBuildContext jobBuildContext) {
        Map<String, Object> contentMap = super.acqTemplateContent(noticeType, noticePhase, jobBuildContext);
        contentMap.put(VERSION_NAME, jobBuildContext.getJobBuild().getVersionName());
        return contentMap;
    }

}