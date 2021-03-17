package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.decorator.jenkins.JenkinsInstanceDecorator;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/3 1:06 下午
 * @Version 1.0
 */
@Component
public class ApplicationEngineDecorator {

    @Resource
    private JenkinsInstanceDecorator jenkinsInstanceDecorator;

    public ApplicationVO.Engine decorator(ApplicationVO.Engine engine, Integer extend) {
        if (extend == 0) return engine;
        jenkinsInstanceDecorator.decorator(engine);
        return engine;
    }
}
