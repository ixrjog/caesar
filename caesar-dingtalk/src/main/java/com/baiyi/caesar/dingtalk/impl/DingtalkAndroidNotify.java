package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/25 3:00 下午
 * @Version 1.0
 */
@Slf4j
@Component("DingtalkAndroidNotify")
public class DingtalkAndroidNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    public static final String ENVIRONMENT_BUILD = "ENVIRONMENT_BUILD";

    public static final String PRODUCT_FLAVOR_BUILD = "PRODUCT_FLAVOR_BUILD";

    @Override
    public String getKey() {
        return JobType.ANDROID.getType();
    }

    @Override
    protected Map<String, Object> acqTemplateContent(int noticeType, int noticePhase, JobBuildContext jobBuildContext) {
        Map<String, Object> contentMap = super.acqTemplateContent(noticeType, noticePhase, jobBuildContext);
        contentMap.put(VERSION_NAME, jobBuildContext.getJobBuild().getVersionName());
        contentMap.put(ENVIRONMENT_BUILD, jobBuildContext.getJobParamDetail().getParamByKey(ENVIRONMENT_BUILD));
        contentMap.put(PRODUCT_FLAVOR_BUILD, jobBuildContext.getJobParamDetail().getParamByKey(PRODUCT_FLAVOR_BUILD));
        return contentMap;
    }

}