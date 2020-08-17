package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:27 下午
 * @Version 1.0
 */
@Slf4j
@Component("DingtalkH5Notify")
public class DingtalkH5Notify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobType.HTML5.getType();
    }

    @Override
    protected Map<String, Object> acqTemplateContent(int noticeType, int noticePhase, JobBuildContext jobBuildContext) {
        Map<String, Object> contentMap = super.acqTemplateContent(noticeType,noticePhase,jobBuildContext);
        //contentMap.put("ccc", "ddd");
        return contentMap;
    }



}