package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.base.BaseDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/17 6:00 下午
 * @Version 1.0
 */
@Component
public class JenkinsInstanceDecorator extends BaseDecorator {

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;


    public void decorator(JenkinsInstanceVO.IInstance iInstance) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(iInstance.getInstanceId());
        if (csJenkinsInstance != null) {
            iInstance.setInstance(BeanCopierUtil.copyProperties(csJenkinsInstance, JenkinsInstanceVO.Instance.class));
        }
    }

    public JenkinsInstanceVO.Instance decorator(JenkinsInstanceVO.Instance instance, Integer extend) {
        instance.setToken("");
        decoratorJenkinsInstance(instance);
        return instance;
    }
}
