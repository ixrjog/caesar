package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.server.ServerDecorator;
import com.baiyi.caesar.decorator.server.ServerGroupDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/17 6:00 下午
 * @Version 1.0
 */
@Component
public class JenkinsInstanceDecorator {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private ServerDecorator serverDecorator;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JenkinsVersionDecorator jenkinsVersionDecorator;

    public void decorator(JenkinsInstanceVO.IInstance iInstance) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(iInstance.getInstanceId());
        if (csJenkinsInstance != null) {
            iInstance.setInstance(BeanCopierUtil.copyProperties(csJenkinsInstance, JenkinsInstanceVO.Instance.class));
        }
    }

    public JenkinsInstanceVO.Instance decorator(JenkinsInstanceVO.Instance instance, Integer extend) {
        instance.setToken("");
        jenkinsVersionDecorator.decorator(instance);
        serverDecorator.decorator(instance);
        serverGroupDecorator.decorator(instance);

        return instance;
    }
}
