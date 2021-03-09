package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.builder.TerminalSessionInstanceBuilder;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.factory.xterm.IXTermProcess;
import com.baiyi.caesar.xterm.handler.RemoteInvokeHandler;
import com.baiyi.caesar.xterm.message.BaseMessage;
import com.baiyi.caesar.xterm.message.InitialIpMessage;
import com.baiyi.caesar.xterm.model.HostSystem;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/15 2:59 下午
 * @Version 1.0
 */
@Component
public class InitialByIpProcess extends BaseProcess implements IXTermProcess {


    /**
     * 初始化XTerm
     *
     * @return
     */
    @Override
    public String getKey() {
        return XTermRequestStatus.INITIAL_IP.getCode();
    }

    @Override
    public void process(String message, Session session, OcTerminalSession ocTerminalSession) {
        InitialIpMessage xtermMessage = (InitialIpMessage) getMessage(message);
        xtermMessage.setLoginUserType(1);
        OcUser ocUser = userFacade.getOcUserBySession();
        String ip = xtermMessage.getIp();

        boolean isAdmin = isOps(ocUser);
        // 鉴权
        if (!isAdmin) {
            OcServer ocServer = ocServerService.queryOcServerByIp(ip);
            OcUserPermission ocUserPermission = new OcUserPermission();
            ocUserPermission.setUserId(ocUser.getId());
            ocUserPermission.setBusinessId(ocServer.getServerGroupId());
            ocUserPermission.setBusinessType(BusinessType.SERVERGROUP.getType());
            OcUserPermission checkUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
            if (checkUserPermission == null)
                return;
        }

        HostSystem hostSystem = buildHostSystem(ocUser, ip, xtermMessage, isAdmin);
        RemoteInvokeHandler.openSSHTermOnSystem(ocTerminalSession.getSessionId(), xtermMessage.getInstanceId(), hostSystem);
        heartbeat(ocTerminalSession.getSessionId());
        terminalFacade.addOcTerminalSessionInstance(TerminalSessionInstanceBuilder.build(ocTerminalSession, hostSystem));
    }


    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, InitialIpMessage.class);
    }

}
