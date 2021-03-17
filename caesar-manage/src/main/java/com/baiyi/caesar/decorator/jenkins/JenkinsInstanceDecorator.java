package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.server.ServerDecorator;
import com.baiyi.caesar.decorator.server.ServerGroupDecorator;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.offbytwo.jenkins.helper.JenkinsVersion;
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


    public JenkinsInstanceVO.Instance decorator(JenkinsInstanceVO.Instance instance, Integer extend) {
        instance.setToken("");
        JenkinsVersion jenkinsVersion = jenkinsServerHandler.getVersion(instance.getName());
        if (jenkinsVersion != null) {
            instance.setVersion(jenkinsVersion.getLiteralVersion());
        }
        if (!IDUtil.isEmpty(instance.getServerId()))
            serverDecorator.decorator(instance);

        if (!IDUtil.isEmpty(instance.getNodeServerGroupId()))
            serverGroupDecorator.decorator(instance);

        return instance;
    }
}
