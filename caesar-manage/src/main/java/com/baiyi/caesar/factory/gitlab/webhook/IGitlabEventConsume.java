package com.baiyi.caesar.factory.gitlab.webhook;

import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:37 上午
 * @Version 1.0
 */
public interface IGitlabEventConsume {

    String getEventKey();

    /**
     * 消费事件
     * @param webhook
     */
    void consumeEvent(GitlabHookVO.Webhook webhook);

   // void consume(CsGitlabWebhook csGitlabWebhook);
}
