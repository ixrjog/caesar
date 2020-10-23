package com.baiyi.caesar.consumer;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;

/**
 * @Author baiyi
 * @Date 2020/10/22 10:49 上午
 * @Version 1.0
 */
public interface GitlabWebhooksConsumer {

    void consumerWebhooks(CsGitlabWebhook csGitlabWebhook);

    void consumerWebhooks();
}
