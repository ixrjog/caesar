package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.facade.ServerCacheFacade;
import com.baiyi.caesar.factory.attribute.impl.AttributeAnsible;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.baiyi.caesar.task.util.TaskUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/10 10:19 上午
 * @Version 1.0
 */
@Component
public class ServerCacheFacadeImpl implements ServerCacheFacade {

    @Resource
    private AttributeAnsible attributeAnsible;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private TaskUtil taskUtil;

    public static final String TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC = "TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC";

    @Override
    public void evictServerCache(OcServer ocServer) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServer.getServerGroupId());
        evictServerGroupCache(ocServerGroup);
    }

    @Override
    public void evictServerGroupCache(OcServerGroup ocServerGroup) {
        attributeAnsible.evictGrouping(ocServerGroup);
        attributeAnsible.evictBuild(ocServerGroup);
        attributeAnsible.evictPreview(ocServerGroup.getId());
        // 通知任务执行
        taskUtil.sendMessage(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC);
    }

}
