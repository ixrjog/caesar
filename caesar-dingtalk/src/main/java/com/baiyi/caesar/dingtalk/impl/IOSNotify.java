package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.dingtalk.content.DingtalkTemplateBuilder;
import com.baiyi.caesar.dingtalk.content.DingtalkTemplateMap;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/19 4:32 下午
 * @Version 1.0
 */
@Slf4j
@Component("IOSNotify")
public class IOSNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobType.IOS.getType();
    }

    @Override
    protected int getBuildType(){
        return BuildType.BUILD.getType();
    }

    @Override
    protected Map<String, Object> acqTemplateContent( int noticePhase, BuildJobContext context) {
        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntries(super.acqTemplateContent(noticePhase, context))
                .paramEntryVersionName(context.getJobBuild().getVersionName())
                .paramEntryBuildDetailsUrl(noticePhase == NoticePhase.END.getType() ? acqBuildDetailsUrl(context.getJobBuild().getId()) : null)
                .build();
        return templateMap.getTemplate();
    }

    // https://caesar.ops.yangege.cn/index.html#/job/build/ios?buildId=168
    private String acqBuildDetailsUrl(int buildId) {
        return Joiner.on("/").join(hostConfig.getUrl(), "index.html#/job/build/ios?buildId=") + buildId;
    }

}