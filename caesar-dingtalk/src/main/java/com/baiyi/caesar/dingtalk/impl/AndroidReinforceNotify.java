package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateBuilder;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateMap;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/31 11:09 上午
 * @Version 1.0
 */
@Slf4j
@Component("AndroidReinforceNotify")
public class AndroidReinforceNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobType.ANDROID_REINFORCE.getType();
    }

    @Override
    protected int getBuildType() {
        return BuildType.DEPLOYMENT.getType();
    }

    @Override
    protected Map<String, Object> buildTemplateContent(int noticePhase, DeploymentJobContext context) {
        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntries(super.buildTemplateContent(noticePhase, context))
                .paramEntryBuildPhase(noticePhase == NoticePhase.START.getType() ? "加固开始" : "加固结束")
                .paramEntryVersionName(noticePhase == NoticePhase.START.getType() ? acqVersionName(context) : null)
                .paramEntryBuildDetailsUrl(noticePhase == NoticePhase.END.getType() ? acqBuildDetailsUrl(context.getJobBuild().getId()) : null)
                .build();

        return templateMap.getTemplate();
    }

    private String acqVersionName(DeploymentJobContext context) {
        CsCiJobBuild csCiJobBuild = acqCiJobBuild(context.getJobBuild().getCiBuildId());
        return csCiJobBuild.getVersionName();
    }

    // https://caesar.example.org/index.html#/job/build/android/reinforce?buildId=168
    private String acqBuildDetailsUrl(int buildId) {
        return Joiner.on("/").join(hostConfig.getUrl(), "index.html#/job/build/android/reinforce?buildId=") + buildId;
    }

}