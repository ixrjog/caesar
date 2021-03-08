package com.baiyi.caesar.ansible.builder;

import com.baiyi.caesar.ansible.bo.ServerTaskMemberBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.generator.caesar.OcServerTaskMember;
import com.baiyi.caesar.domain.generator.caesar.OcServer;

/**
 * @Author baiyi
 * @Date 2020/4/8 2:05 下午
 * @Version 1.0
 */
public class ServerTaskMemberBuilder {

    public static OcServerTaskMember build(OcServerTask ocServerTask, String hostPattern, String manageIp, OcServer ocServer) {
        ServerTaskMemberBO serverTaskMemberBO = ServerTaskMemberBO.builder()
                .taskId(ocServerTask.getId())
                .hostPattern(hostPattern)
                .manageIp(manageIp)
                .serverId(ocServer.getId())
                .envType(ocServer.getEnvType())
                .build();
        return covert(serverTaskMemberBO);
    }

    private static OcServerTaskMember covert(ServerTaskMemberBO serverTaskMemberBO) {
        return BeanCopierUtil.copyProperties(serverTaskMemberBO, OcServerTaskMember.class);
    }
}
