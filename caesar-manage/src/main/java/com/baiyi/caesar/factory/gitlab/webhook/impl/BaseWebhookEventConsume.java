package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.builder.gitlab.GitlabWebhookBuilder;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.baiyi.caesar.domain.vo.tag.BusinessTagVO;
import com.baiyi.caesar.factory.gitlab.webhook.GitlabEventConsumeFactory;
import com.baiyi.caesar.factory.gitlab.webhook.IGitlabEventConsume;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.tag.OcBusinessTagService;
import com.baiyi.caesar.service.tag.OcTagService;
import com.baiyi.caesar.service.user.OcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:43 上午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseWebhookEventConsume implements IGitlabEventConsume, InitializingBean {

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private CsGitlabWebhookService csGitlabWebhookService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private OcTagService ocTagService;

    @Resource
    private OcBusinessTagService ocBusinessTagService;

    @Resource
    protected CsCiJobService csCiJobService;

    public static final String AUTO_BUILD = "AutoBuild";

    /**
     * 消费事件
     *
     * @param webhook
     */
    @Override
    // @Async(value = Global.TaskPools.COMMON)
    public void consumeEvent(GitlabHookVO.Webhook webhook) {
        CsGitlabInstance instance = GitlabUtil.filterInstance(csGitlabInstanceService.queryAll(), webhook);
        if (instance == null)
            return;
        CsGitlabWebhook csGitlabWebhook = saveEvent(instance, webhook);
        log.info("GitlabEvent: id = {}", csGitlabWebhook.getId());
        consume(csGitlabWebhook);
    }

    protected CsGitlabInstance getGitlabInstanceById(int instanceId) {
        return csGitlabInstanceService.queryCsGitlabInstanceById(instanceId);
    }

    protected CsGitlabProject getProject(CsGitlabWebhook csGitlabWebhook) {
        return csGitlabProjectService.queryCsGitlabProjectByUniqueKey(csGitlabWebhook.getInstanceId(), csGitlabWebhook.getProjectId());
    }

    protected void updateEvent(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookService.updateCsGitlabWebhook(csGitlabWebhook);
    }

    private CsGitlabWebhook saveEvent(CsGitlabInstance instance, GitlabHookVO.Webhook webhook) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(webhook.getUser_username());
        CsGitlabWebhook csGitlabWebhook = GitlabWebhookBuilder.build(webhook, instance, ocUser);
        csGitlabWebhookService.addCsGitlabWebhook(csGitlabWebhook);
        return csGitlabWebhook;
    }

    protected void consumed(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhook.setIsConsumed(true);
        csGitlabWebhookService.updateCsGitlabWebhook(csGitlabWebhook);
    }

    protected boolean isAuthBuild(int projectId) {
        OcTag ocTag = ocTagService.queryOcTagByKey(AUTO_BUILD);
        if (ocTag == null)
            return false;
        BusinessTagVO.BusinessTag businessTag = BusinessTagVO.BusinessTag.builder()
                .businessType(BusinessType.GITLAB_PROJECT.getType())
                .businessId(projectId)
                .tagId(ocTag.getId())
                .build();
        return ocBusinessTagService.queryOcBusinessTagByUniqueKey(businessTag) != null;
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        GitlabEventConsumeFactory.register(this);
    }
}
