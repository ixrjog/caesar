package com.baiyi.caesar.factory.xterm.impl;

import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.factory.xterm.ITerminalProcess;
import com.baiyi.caesar.xterm.message.BaseMessage;
import com.baiyi.caesar.xterm.message.BatchCommandMessage;
import com.baiyi.caesar.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 7:22 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class BatchCommandProcess extends BaseProcess implements ITerminalProcess {

    /**
     * XTerm设置批量命令
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.BATCH_COMMAND.getCode();
    }

    @Override
    public void process(String message, Session session, OcTerminalSession ocTerminalSession) {
        BatchCommandMessage xtermMessage = (BatchCommandMessage) getMessage(message);
        JSchSessionMap.setBatch(ocTerminalSession.getSessionId(), xtermMessage.getIsBatch());
    }

    @Override
    protected BaseMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, BatchCommandMessage.class);
    }

}
