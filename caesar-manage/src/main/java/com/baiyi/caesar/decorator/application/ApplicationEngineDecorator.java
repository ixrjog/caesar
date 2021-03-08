package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
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
    private CsJenkinsInstanceService csJenkinsInstanceService;

    public ApplicationVO.Engine decorator(ApplicationVO.Engine engine, Integer extend) {
        if (extend == 0) return engine;
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(engine.getJenkinsInstanceId());
        if (csJenkinsInstance != null) {
            engine.setInstance(BeanCopierUtil.copyProperties(csJenkinsInstance, JenkinsInstanceVO.Instance.class));
        }
        return engine;
    }
}
