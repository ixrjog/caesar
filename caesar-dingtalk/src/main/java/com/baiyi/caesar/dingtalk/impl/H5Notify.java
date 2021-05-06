package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.base.BuildType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:27 下午
 * @Version 1.0
 */
@Slf4j
@Component("H5Notify")
public class H5Notify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobTypeEnum.HTML5.getType();
    }

    @Override
    protected int getBuildType(){
        return BuildType.BUILD.getType();
    }

}