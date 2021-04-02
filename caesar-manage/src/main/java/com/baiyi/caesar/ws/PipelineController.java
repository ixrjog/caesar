package com.baiyi.caesar.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.caesar.common.base.PipelineStatus;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.facade.AuthBaseFacade;
import com.baiyi.caesar.facade.jenkins.PipelineFacade;
import com.baiyi.caesar.jenkins.message.InitialMessage;
import com.baiyi.caesar.jenkins.message.QueryMessage;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author baiyi
 * @Date 2021/3/31 1:34 下午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/ws/pipeline")
@Component
public class PipelineController {

    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
    // 当前会话 uuid
    private final String sessionId = UUID.randomUUID().toString();

    private Session session = null;

    private String username = null;

    // 超时时间1H
    public static final Long WEBSOCKET_TIMEOUT = 60 * 60 * 1000L;

    private static PipelineFacade pipelineFacade;

    private static AuthBaseFacade authBaseFacade;

    @Autowired
    public void setPipelineFacade(PipelineFacade pipelineFacade) {
        PipelineController.pipelineFacade = pipelineFacade;
    }


    @Autowired
    public void setAuthBaseFacade(AuthBaseFacade authBaseFacade) {
        PipelineController.authBaseFacade = authBaseFacade;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        // this.sessionId = UUID.randomUUID().toString();
        log.info("终端会话尝试链接，sessionId = {}", sessionId);
        sessionSet.add(session);
        int cnt = onlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        this.session = session;
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

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || StringUtils.isEmpty(message)) return;
        // log.info("来自客户端的消息：{}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        String status = jsonObject.getString("status");
        // 鉴权并更新会话信息
        if (PipelineStatus.INITIAL.getCode().equals(status)) {
            InitialMessage initialMessage = buildInitialMessage(message);
            String username = authBaseFacade.getUserByToken(initialMessage.getToken());
            SessionUtil.setUsername(username); // 设置当前会话用户身份
            this.username = username;
            return;
        }
        if (PipelineStatus.QUERY_TASK.getCode().equals(status)) {
            Instant inst1 = Instant.now();
            log.info("启动成功! 耗时:{}/s", Duration.between(inst1, Instant.now()).getSeconds());

            QueryMessage queryMessage = buildQueryMessage(message);
            List<JenkinsPipelineVO.Pipeline> pipelines;
            if (queryMessage.getBuildType() == 0) {
                pipelines = pipelineFacade.queryBuildJobPipelines(this.username);
            } else {
                pipelines = pipelineFacade.queryDeploymentJobPipelines(this.username);
            }
            String jsonStr = JSON.toJSONString(pipelines);
            try {
                session.getBasicRemote().sendText(jsonStr);
                log.info("查询流水线任务详情! username = {}, buildType = {}, 耗时:{}/s", this.username, queryMessage.getBuildType(), Duration.between(inst1, Instant.now()).getSeconds());
            } catch (IOException e) {
                log.error("发送数据错误！ sessionId = {}", sessionId);
            }

        }
    }

    private QueryMessage buildQueryMessage(String message) {
        return new GsonBuilder().create().fromJson(message, QueryMessage.class);
    }


    private InitialMessage buildInitialMessage(String message) {
        return new GsonBuilder().create().fromJson(message, InitialMessage.class);
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
