package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.factory.xterm.IXTermProcess;
import com.baiyi.caesar.xterm.message.BaseMessage;
import com.baiyi.caesar.xterm.model.JSchSession;
import com.baiyi.caesar.xterm.model.JSchSessionMap;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Date;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:38 上午
 * @Version 1.0
 */
@Component
public class CloseProcess extends BaseProcess implements IXTermProcess {

    /**
     * 关闭所有XTerm
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.CLOSE.getCode();
    }

    @Override
    public void process(String message, Session session, OcTerminalSession ocTerminalSession) {
        Map<String, JSchSession> sessionMap = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId());
        if (sessionMap == null) return;
        for (String instanceId : sessionMap.keySet())
            try {
                JSchSession jSchSession = sessionMap.get(instanceId);
                jSchSession.getChannel().disconnect();
                writeAuditLog(ocTerminalSession, instanceId); // 写审计日志
                //  writeCommanderLog(jSchSession.getCommanderLog(),ocTerminalSession, instanceId); // 写命令日志
                closeSessionInstance(ocTerminalSession, instanceId); // 设置关闭会话
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            sessionMap.clear();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ocTerminalSession.setCloseTime(new Date());
        ocTerminalSession.setIsClosed(true);
        terminalFacade.updateOcTerminalSession(ocTerminalSession);
        ocTerminalSession = null;
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return null;
    }
}
