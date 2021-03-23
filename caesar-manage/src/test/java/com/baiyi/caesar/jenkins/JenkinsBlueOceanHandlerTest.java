package com.baiyi.caesar.jenkins;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.jenkins.handler.JenkinsBlueHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/3/23 2:36 下午
 * @Version 1.0
 */
public class JenkinsBlueOceanHandlerTest extends BaseUnit {

    @Resource
    private JenkinsBlueHandler jenkinsBlueOceanHandler;

    @Test
    void ddd(){
        jenkinsBlueOceanHandler.test1();
    }
}
