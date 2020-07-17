package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.OcServerChangeTaskFlow;
import com.baiyi.caesar.domain.vo.serverChange.ServerChangeTaskVO;
import com.baiyi.caesar.service.serverChange.OcServerChangeTaskFlowService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/3 10:05 上午
 * @Version 1.0
 */
@Component("ServerChangeTaskDecorator")
public class ServerChangeTaskDecorator {

    @Resource
    private OcServerChangeTaskFlowService ocServerChangeTaskFlowService;

    public ServerChangeTaskVO.ServerChangeTask decorator(ServerChangeTaskVO.ServerChangeTask serverChangeTask) {

        List<OcServerChangeTaskFlow> taskFlowList = ocServerChangeTaskFlowService.queryOcServerChangeTaskFlowByTaskId(serverChangeTask.getTaskId());
        if (taskFlowList != null)
            serverChangeTask.setTaskFlows(BeanCopierUtils.copyListProperties(taskFlowList, ServerChangeTaskVO.ServerChangeTaskFlow.class));

        return serverChangeTask;
    }
}
