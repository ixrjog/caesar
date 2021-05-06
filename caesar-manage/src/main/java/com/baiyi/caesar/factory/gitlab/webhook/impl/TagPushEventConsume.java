package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.factory.gitlab.webhook.IWebhookEventConsume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/6 4:31 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TagPushEventConsume extends BaseWebhookEventConsume implements IWebhookEventConsume {

    @Override
    public String getEventKey() {
        return GitlabEventType.TAG_PUSH.getDesc();
    }

    @Override
    public void consume(CsGitlabWebhook csGitlabWebhook) {
    }
}
