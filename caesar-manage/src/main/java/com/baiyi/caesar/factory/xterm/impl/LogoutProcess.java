package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.factory.xterm.ITerminalProcess;
import com.baiyi.caesar.xterm.message.BaseMessage;
import com.baiyi.caesar.xterm.message.LogoutMessage;
import com.baiyi.caesar.xterm.model.JSchSession;
import com.baiyi.caesar.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 5:19 下午
 * @Version 1.0
 */
@Component
public class LogoutProcess extends BaseProcess implements ITerminalProcess {

    /**
     * 初始化XTerm 单个关闭
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.LOGOUT.getCode();
    }

    @Override
    public void process(String message, Session session, OcTerminalSession ocTerminalSession) {
        LogoutMessage baseMessage = (LogoutMessage) getMessage(message);
        writeAuditLog(ocTerminalSession, baseMessage.getInstanceId()); // 写审计日志
        closeSessionInstance(ocTerminalSession, baseMessage.getInstanceId()); // 设置关闭会话

        JSchSession jSchSession = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId(), baseMessage.getInstanceId());
        jSchSession.getChannel().disconnect();
        jSchSession.setCommander(null);
        jSchSession.setChannel(null);
        jSchSession.setInputToChannel(null);
        jSchSession.setTermSessionId(null);
        jSchSession.setSessionOutput(null);
        jSchSession.setInstanceId(null);
        jSchSession.setHostSystem(null);
        jSchSession = null;
        JSchSessionMap.removeSession(ocTerminalSession.getSessionId(), baseMessage.getInstanceId());
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, LogoutMessage.class);
    }
}
