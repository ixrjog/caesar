package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.base.BuildType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/2 3:01 下午
 * @Version 1.0
 */
@Slf4j
@Component("DockerNotify")
public class DockerNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    @Override
    public String getKey() {
        return JobTypeEnum.DOCKER.getType();
    }

    protected void beforeEvent() {

    }

    @Override
    protected int getBuildType() {
        return BuildType.BUILD.getType();
    }
}
