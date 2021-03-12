package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.factory.xterm.ITerminalProcess;
import com.baiyi.caesar.xterm.handler.RemoteInvokeHandler;
import com.baiyi.caesar.xterm.message.BaseMessage;
import com.baiyi.caesar.xterm.message.ResizeMessage;
import com.baiyi.caesar.xterm.model.JSchSession;
import com.baiyi.caesar.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.ChannelShell;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/12 10:43 上午
 * @Version 1.0
 */
@Component
public class ResizeProcess extends BaseProcess implements ITerminalProcess {

    /**
     * XTerm改变形体
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.RESIZE.getCode();
    }

    @Override
    public void process(String message, Session session, OcTerminalSession ocTerminalSession) {
        ResizeMessage xtermMessage= (ResizeMessage) getMessage(message);
        try {
            JSchSession jSchSession = JSchSessionMap.getBySessionId(ocTerminalSession.getSessionId(), xtermMessage.getInstanceId());
            RemoteInvokeHandler.invokeChannelPtySize((ChannelShell)jSchSession.getChannel(),xtermMessage);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ResizeMessage.class);
    }
}

