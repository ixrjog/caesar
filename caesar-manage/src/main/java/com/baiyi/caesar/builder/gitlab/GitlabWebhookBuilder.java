package com.baiyi.caesar.builder.gitlab;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.bo.gitlab.GitlabWebhookBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHooksVO;

/**
 * @Author baiyi
 * @Date 2020/10/21 11:01 上午
 * @Version 1.0
 */
public class GitlabWebhookBuilder {

    public static CsGitlabWebhook build(GitlabHooksVO.Webhooks webhooks, CsGitlabInstance csGitlabInstance, OcUser ocUser) {
        GitlabWebhookBO bo = GitlabWebhookBO.builder()
                .instanceId(csGitlabInstance.getId())
                .name(webhooks.getProject().getName())
                .projectId(webhooks.getProject_id())
                .objectKind(webhooks.getObject_kind())
                .afterCommit(webhooks.getAfter())
                .beforeCommit(webhooks.getBefore())
                .ref(webhooks.getRef())
                .sshUrl(webhooks.getProject().getSsh_url())
                .webUrl(webhooks.getProject().getWeb_url())
                .httpUrl(webhooks.getProject().getHttp_url())
                .homepage(webhooks.getProject().getHomepage())
                .totalCommitsCount(webhooks.getTotal_commits_count())
                .hooksContent(JSON.toJSONString(webhooks))
                .username(ocUser != null ? ocUser.getUsername() : webhooks.getUser_username())
                .userEmail(ocUser != null ? ocUser.getEmail() : webhooks.getUser_email())
                .userId(ocUser != null ? ocUser.getId() : 0)
                .build();
        return covert(bo);
    }

    private static CsGitlabWebhook covert(GitlabWebhookBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsGitlabWebhook.class);
    }
}
