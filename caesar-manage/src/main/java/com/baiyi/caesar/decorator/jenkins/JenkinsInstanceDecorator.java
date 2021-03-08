package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.baiyi.caesar.service.server.OcServerService;
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
    private OcServerService ocServerService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    public JenkinsInstanceVO.Instance decorator(JenkinsInstanceVO.Instance instance, Integer extend) {
        instance.setToken("");
        JenkinsVersion jenkinsVersion = jenkinsServerHandler.getVersion(instance.getName());
        if (jenkinsVersion != null) {
            instance.setVersion(jenkinsVersion.getLiteralVersion());
        }
        if (!IDUtil.isEmpty(instance.getServerId())) {
            OcServer ocServer = ocServerService.queryOcServerById(instance.getServerId());
            if (ocServer != null)
                instance.setServer(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
        }
        if (!IDUtil.isEmpty(instance.getNodeServerGroupId())) {
            OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(instance.getNodeServerGroupId());
            if (ocServerGroup != null)
                instance.setNodeGroup(BeanCopierUtil.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class));
        }
        return instance;
    }
}
