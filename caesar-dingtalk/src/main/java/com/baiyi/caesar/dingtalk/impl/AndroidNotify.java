package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/25 3:00 下午
 * @Version 1.0
 */
@Slf4j
@Component("AndroidNotify")
public class AndroidNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    public static final String BUILD_TYPE = "BUILD_TYPE"; // 构建环境

    public static final String PRODUCT_FLAVOR = "PRODUCT_FLAVOR"; // 构建渠道

    @Override
    public String getKey() {
        return JobType.ANDROID.getType();
    }

    @Override
    protected int getBuildType(){
        return BuildType.BUILD.getType();
    }

    @Override
    protected Map<String, Object> acqTemplateContent(int noticePhase, BuildJobContext context) {
        Map<String, Object> contentMap = super.acqTemplateContent(noticePhase, context);
        contentMap.put(VERSION_NAME, context.getJobBuild().getVersionName());
        contentMap.put(BUILD_TYPE, context.getJobParamDetail().getParamByKey(BUILD_TYPE));
        contentMap.put(PRODUCT_FLAVOR, context.getJobParamDetail().getParamByKey(PRODUCT_FLAVOR));
        if (noticePhase == NoticePhase.END.getType()) {
            contentMap.put(BUILD_DETAILS_URL, acqBuildDetailsUrl(context.getJobBuild().getId()));
        }
        return contentMap;
    }

    // https://caesar.ops.yangege.cn/index.html#/job/build/android?buildId=168
    private String acqBuildDetailsUrl(int buildId) {
        return Joiner.on("/").join(hostConfig.getUrl(), "index.html#/job/build/android?buildId=") + buildId;
    }

}