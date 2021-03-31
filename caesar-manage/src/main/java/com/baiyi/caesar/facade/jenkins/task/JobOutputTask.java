package com.baiyi.caesar.facade.jenkins.task;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.facade.jenkins.impl.EngineFacadeImpl;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/3/22 10:11 上午
 * @Version 1.0
 */
@Slf4j
public class JobOutputTask implements Runnable {

    Session session;
    String sessionId;

    private static final long SLEEP = 10000L;

    public JobOutputTask(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (session.isOpen()) {
            try {
                if (EngineFacadeImpl.chart != null) {
                    session.getBasicRemote().sendText(JSON.toJSONString(EngineFacadeImpl.chart));
                }
                Thread.sleep(SLEEP);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }
}