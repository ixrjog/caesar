package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.factory.xterm.ITerminalProcess;
import com.baiyi.caesar.xterm.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/13 6:50 下午
 * @Version 1.0 HEARTBEAT
 */
@Slf4j
@Component
public class HeartbeatProcess extends BaseProcess implements ITerminalProcess {

    /**
     * XTerm心跳
     *
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.HEARTBEAT.getCode();
    }

    @Override
    public void process(String message, Session session, OcTerminalSession ocTerminalSession) {
        //  log.info("收到前端心跳");
        heartbeat(ocTerminalSession.getSessionId());
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return null;
    }

}
