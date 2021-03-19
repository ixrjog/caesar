package com.baiyi.caesar.ws;

import com.baiyi.caesar.facade.jenkins.task.SentEngineChartTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/3/18 11:08 上午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/ws/engine")
@Component
public final class EngineController {

    /**
     * 引擎视图
     */

    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    private Session session = null;

    // 当前会话 uuid
    private final String sessionId = UUID.randomUUID().toString();

    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        sessionSet.add(session);
        log.info("连接成功！");
        // 线程启动
        Runnable run = new SentEngineChartTask(sessionId, session);
        Thread thread = new Thread(run);
        thread.start();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        sessionSet.remove(session);
        int cnt = onlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

}
