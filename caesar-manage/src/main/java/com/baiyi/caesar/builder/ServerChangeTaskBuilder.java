package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.ServerChangeTaskBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerChangeTask;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:45 下午
 * @Version 1.0
 */
public class ServerChangeTaskBuilder {

    public static OcServerChangeTask build(OcServer ocServer,String changeType,String taskId) {
        ServerChangeTaskBO bo = ServerChangeTaskBO.builder()
                .taskId(taskId)
                .serverId(ocServer.getId())
                .serverGroupId(ocServer.getServerGroupId())
                .changeType(changeType)
                .build();
        return covert(bo);
    }

    private static OcServerChangeTask covert(ServerChangeTaskBO bo) {
        return BeanCopierUtil.copyProperties(bo, OcServerChangeTask.class);
    }
}
