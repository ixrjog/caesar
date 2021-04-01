package com.baiyi.caesar.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baiyi.caesar.common.base.XTermRequestStatus;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2021/3/22 10:09 上午
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/ws/job/output")
@Component
public final class JobOutputController {

    /**
     * 引擎视图
     */
    private final String sessionId = UUID.randomUUID().toString();

    // private Session session = null;

    private static JobFacade jobFacade;

    @Autowired
    public void setJobFacade(JobFacade jobFacade) {
        JobOutputController.jobFacade = jobFacade;
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
        JSONObject jsonObject = JSON.parseObject(message);
        String status = jsonObject.getString("status");
        if (XTermRequestStatus.INITIAL.getCode().equals(status)) {
            Integer buildId = jsonObject.getInteger("buildId");
            Integer buildType = jsonObject.getInteger("buildType");
            if (IDUtil.isEmpty(buildId)) return;
            log.info("任务日志订阅启动: sessionId ={} , buildType = {} , buildId = {}", sessionId, buildType, buildId);
            jobFacade.buildOutput(buildType, buildId, session);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("任务日志订阅关闭: sessionId ={} ", sessionId);
    }

}
