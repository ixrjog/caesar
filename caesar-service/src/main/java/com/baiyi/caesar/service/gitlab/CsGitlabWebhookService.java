package com.baiyi.caesar.service.gitlab;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;

/**
 * @Author baiyi
 * @Date 2020/10/21 10:39 上午
 * @Version 1.0
 */
public interface CsGitlabWebhookService {

    CsGitlabWebhook queryOneCsGitlabWebhookByObjectKind(String objectKind);

    void addCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook);

    void updateCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook);
}
