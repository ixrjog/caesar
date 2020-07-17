package com.baiyi.caesar.factory.xterm;

import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
public interface IXTermProcess {

    void xtermProcess(String message, Session session, OcTerminalSession ocTerminalSession);

    String getKey();

}
