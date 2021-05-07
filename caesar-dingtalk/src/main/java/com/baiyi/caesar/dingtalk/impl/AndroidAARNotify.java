package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateBuilder;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateMap;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/7 10:26 上午
 * @Version 1.0
 */
@Slf4j
@Component("AndroidAARNotify")
public class AndroidAARNotify extends BaseDingtalkNotify implements IDingtalkNotify {


    @Override
    public String getKey() {
        return JobTypeEnum.ANDROID_AAR.getType();
    }

    @Override
    protected int getBuildType() {
        return BuildType.BUILD.getType();
    }

    @Override
    protected Map<String, Object> buildTemplateContent(int noticePhase, BuildJobContext context) {
        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntries(super.buildTemplateContent(noticePhase, context))
                .paramEntryByVersionName(context.getJobBuild().getVersionName())
                .paramEntryByBuildDetailsUrl(noticePhase == NoticePhase.END.getType() ? acqBuildDetailsUrl(context.getJobBuild().getId()) : null)
                .build();

        return templateMap.getTemplate();
    }

    // https://caesar.example.org/index.html#/job/build/android?buildId=168
    private String acqBuildDetailsUrl(int buildId) {
        return Joiner.on("/").join(hostConfig.getUrl(), "index.html#/job/build/android?buildId=") + buildId;
    }


}
