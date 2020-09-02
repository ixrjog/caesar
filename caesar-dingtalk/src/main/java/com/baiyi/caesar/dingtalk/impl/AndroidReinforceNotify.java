package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
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
    protected Map<String, Object> acqTemplateContent(int noticePhase, DeploymentJobContext context) {
        Map<String, Object> contentMap = super.acqTemplateContent(noticePhase, context);
        if (noticePhase == NoticePhase.START.getType()) {
            CsCiJobBuild csCiJobBuild = acqCiJobBuild(context.getJobBuild().getCiBuildId());
            contentMap.put(VERSION_NAME, csCiJobBuild.getVersionName());
        } else {
            contentMap.put(BUILD_DETAILS_URL, acqBuildDetailsUrl(context.getJobBuild().getId()));
        }
        return contentMap;
    }

    // https://caesar.ops.yangege.cn/index.html#/job/build/android/reinforce?buildId=168
    private String acqBuildDetailsUrl(int buildId) {
        return Joiner.on("/").join(hostConfig.getUrl(), "index.html#/job/build/android/reinforce?buildId=") + buildId;
    }

}