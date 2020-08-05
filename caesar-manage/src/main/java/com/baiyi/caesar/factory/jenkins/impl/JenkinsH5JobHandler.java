package com.baiyi.caesar.factory.jenkins.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.factory.jenkins.IJenkinsJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/8/5 4:59 下午
 * @Version 1.0
 */
@Slf4j
@Component("JenkinsH5JobHandler")
public class JenkinsH5JobHandler extends BaseJenkinsJobHandler implements IJenkinsJobHandler {

    @Override
    public String getKey() {
        return JobType.HTML5.getType();
    }


}
