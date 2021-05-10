package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.factory.gitlab.webhook.GitlabEventConsumeFactory;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/10 3:27 下午
 * @Version 1.0
 */
class TagPushEventConsumeTest extends BaseUnit {

    @Resource
    private CsGitlabWebhookService gitlabWebhookService;

    @Test
    void consumeEventTest() {
        CsGitlabWebhook hook = gitlabWebhookService.getById(22251);
        GitlabEventConsumeFactory.getEventConsumeByKey(GitlabEventType.TAG_PUSH.getDesc()).consume(hook);
    }

}