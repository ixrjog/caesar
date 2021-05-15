package com.baiyi.caesar.packer.application;

import com.baiyi.caesar.packer.jenkins.JenkinsInstancePacker;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/3 1:06 下午
 * @Version 1.0
 */
@Component
public class ApplicationEnginePacker {

    @Resource
    private JenkinsInstancePacker jenkinsInstancePacker;

    public ApplicationVO.Engine decorator(ApplicationVO.Engine engine, Integer extend) {
        if (extend == 0) return engine;
        jenkinsInstancePacker.wrap(engine);
        return engine;
    }
}
