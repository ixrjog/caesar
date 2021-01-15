package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/18 5:33 下午
 * @Version 1.0
 */
@Slf4j
@Component("JavaNotify")
public class JavaNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobType.JAVA.getType();
    }

    @Override
    protected int getBuildType(){
        return BuildType.BUILD.getType();
    }

    @Override
    protected Map<String, Object> acqTemplateContent( int noticePhase, BuildJobContext jobBuildContext) {
        Map<String, Object> contentMap = super.acqTemplateContent( noticePhase, jobBuildContext);
        contentMap.put(VERSION_NAME, jobBuildContext.getJobBuild().getVersionName());
        return contentMap;
    }

}