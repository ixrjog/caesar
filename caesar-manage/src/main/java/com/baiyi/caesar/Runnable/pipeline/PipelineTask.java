package com.baiyi.caesar.Runnable.pipeline;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.common.util.InstantUtil;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.facade.jenkins.PipelineFacade;
import com.baiyi.caesar.jenkins.message.InitialMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.websocket.Session;
import java.time.Instant;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/4/2 10:50 上午
 * @Version 1.0
 */
@Slf4j(topic = "PipelineTask")
public class PipelineTask implements Runnable {

    Session session;
    String sessionId;

    private PipelineFacade pipelineFacade;

    public PipelineTask(String sessionId, Session session, PipelineFacade pipelineFacade) {
        this.sessionId = sessionId;
        this.session = session;
        this.pipelineFacade = pipelineFacade;
    }

    @Override
    public void run() {
        while (session.isOpen()) {
            try {
                PipelineSessionUtil.PipelineContext context = PipelineSessionUtil.getUserBySessionId(this.sessionId);
                if (context != null && context.getBuildType() != null) {
                    Instant inst = Instant.now();
                    List<JenkinsPipelineVO.Pipeline> pipelines;
                    String queryUsername = InitialMessage.QueryType.ALL.equalsIgnoreCase(context.getQueryType()) ? null : context.getUsername();
                    if (context.getBuildType() == 0) {
                        pipelines = pipelineFacade.queryBuildJobPipelines(queryUsername, context.getQuerySize());
                    } else {
                        pipelines = pipelineFacade.queryDeploymentJobPipelines(queryUsername, context.getQuerySize());
                    }
                    if (CollectionUtils.isNotEmpty(pipelines)) {
                        String jsonStr = JSON.toJSONString(pipelines);
                        session.getBasicRemote().sendText(jsonStr);
                        String buildTypeStr = context.getBuildType() == 0 ? "构建" : "部署";
                        log.info("推送{}流水线任务详情! sessionId = {}, username = {}, 耗时: {}/ms", buildTypeStr, this.sessionId, context.getUsername(), InstantUtil.timerMillis(inst));
                    }
                }
                Thread.sleep(4000);
            } catch (Exception ex) {
                //  log.error(ex.toString(), ex);
            }
        }
    }
}