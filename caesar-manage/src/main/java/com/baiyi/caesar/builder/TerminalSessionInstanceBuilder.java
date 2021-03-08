package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.TerminalSessionInstanceBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSessionInstance;
import com.baiyi.caesar.xterm.model.HostSystem;

/**
 * @Author baiyi
 * @Date 2020/5/25 11:21 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceBuilder {

    public static OcTerminalSessionInstance build(OcTerminalSession ocTerminalSession, HostSystem hostSystem) {
        TerminalSessionInstanceBO terminalSessionInstanceBO = TerminalSessionInstanceBO.builder()
                .sessionId(ocTerminalSession.getSessionId())
                .instanceId(hostSystem.getInstanceId())
                .systemUser(hostSystem.getSshKeyCredential().getSystemUser())
                .hostIp(hostSystem.getHost())
                .build();
        return covert(terminalSessionInstanceBO);
    }

    public static OcTerminalSessionInstance build(OcTerminalSession ocTerminalSession, HostSystem hostSystem, String duplicateInstanceId) {
        OcTerminalSessionInstance ocTerminalSessionInstance = build(ocTerminalSession, hostSystem);
        ocTerminalSessionInstance.setDuplicateInstanceId(duplicateInstanceId);
        return ocTerminalSessionInstance;
    }

    private static OcTerminalSessionInstance covert(TerminalSessionInstanceBO terminalSessionInstanceBO) {
        return BeanCopierUtil.copyProperties(terminalSessionInstanceBO, OcTerminalSessionInstance.class);
    }
}
