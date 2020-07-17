package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.ServerChangeTaskFlowBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.OcServerChangeTask;
import com.baiyi.caesar.domain.generator.caesar.OcServerChangeTaskFlow;

/**
 * @Author baiyi
 * @Date 2020/5/28 3:05 下午
 * @Version 1.0
 */
public class ServerChangeTaskFlowBuilder {

    public static OcServerChangeTaskFlow build(OcServerChangeTask ocServerChangeTask,String flowName) {
        ServerChangeTaskFlowBO bo = ServerChangeTaskFlowBO.builder()
                .taskFlowName(flowName)
                .taskId(ocServerChangeTask.getTaskId())
                .build();
        return covert(bo);
    }

    public static OcServerChangeTaskFlow build(OcServerChangeTask ocServerChangeTask,String flowName, int parentId) {
        ServerChangeTaskFlowBO bo = ServerChangeTaskFlowBO.builder()
                .taskFlowName(flowName)
                .taskId(ocServerChangeTask.getTaskId())
                .flowParentId(parentId)
                .build();
        return covert(bo);
    }

    private static OcServerChangeTaskFlow covert(ServerChangeTaskFlowBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcServerChangeTaskFlow.class);
    }
}
