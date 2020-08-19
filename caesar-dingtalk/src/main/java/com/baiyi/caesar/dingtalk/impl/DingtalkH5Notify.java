package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

}