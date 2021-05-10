package com.baiyi.caesar.service.gitlab;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.domain.param.gitlab.GitlabEventParam;

/**
 * @Author baiyi
 * @Date 2020/10/21 10:39 上午
 * @Version 1.0
 */
public interface CsGitlabWebhookService {

    CsGitlabWebhook getById(int id);

    void addCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook);

    void updateCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook);

    DataTable<CsGitlabWebhook> queryCsGitlabWebhookByParam(GitlabEventParam.GitlabEventPageQuery pageQuery);
}
