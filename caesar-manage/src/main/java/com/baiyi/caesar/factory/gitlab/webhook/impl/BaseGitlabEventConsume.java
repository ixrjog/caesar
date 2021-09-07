package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.builder.gitlab.GitlabWebhookBuilder;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.dingtalk.config.DingtalkConfig;
import com.baiyi.caesar.dingtalk.handler.DingtalkHandler;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.baiyi.caesar.domain.vo.tag.BusinessTagVO;
import com.baiyi.caesar.factory.gitlab.webhook.GitlabEventConsumeFactory;
import com.baiyi.caesar.factory.gitlab.webhook.IGitlabEventConsume;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.baiyi.caesar.service.dingtalk.CsDingtalkTemplateService;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.tag.OcBusinessTagService;
import com.baiyi.caesar.service.tag.OcTagService;
import com.baiyi.caesar.service.user.OcUserService;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
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
public abstract class BaseGitlabEventConsume implements IGitlabEventConsume, InitializingBean {

    @Resource
    private CsGitlabInstanceService gitlabInstanceService;

    @Resource
    private OcUserService userService;

    @Resource
    private CsGitlabWebhookService gitlabWebhookService;

    @Resource
    protected CsGitlabProjectService gitlabProjectService;

    @Resource
    private OcTagService ocTagService;

    @Resource
    private OcBusinessTagService businessTagService;

    @Resource
    protected CsCiJobService ciJobService;

    @Resource
    protected CsApplicationScmMemberService applicationScmMemberService;


    public static final String AUTO_BUILD = "AutoBuild";

    /**
     * 消费事件
     *
     * @param webhook
     */
    @Override
    // @Async(value = Global.TaskPools.COMMON)
    public void consumeEvent(GitlabHookVO.Webhook webhook) {
        CsGitlabInstance instance = GitlabUtil.filterInstance(gitlabInstanceService.queryAll(), webhook);
        if (instance == null)
            return;
        CsGitlabWebhook csGitlabWebhook = saveEvent(instance, webhook);
        log.info("GitlabEvent: id = {}", csGitlabWebhook.getId());
        consume(csGitlabWebhook);
        doNotify(webhook);
    }

    protected void doNotify(GitlabHookVO.Webhook webhook){
    }

    abstract protected void consume(CsGitlabWebhook csGitlabWebhook);

    protected CsGitlabInstance getGitlabInstanceById(int instanceId) {
        return gitlabInstanceService.queryCsGitlabInstanceById(instanceId);
    }

    protected CsGitlabProject getProject(CsGitlabWebhook csGitlabWebhook) {
        return gitlabProjectService.queryCsGitlabProjectByUniqueKey(csGitlabWebhook.getInstanceId(), csGitlabWebhook.getProjectId());
    }

    protected void updateEvent(CsGitlabWebhook csGitlabWebhook) {
        gitlabWebhookService.updateCsGitlabWebhook(csGitlabWebhook);
    }

    private CsGitlabWebhook saveEvent(CsGitlabInstance instance, GitlabHookVO.Webhook webhook) {
        OcUser ocUser = userService.queryOcUserByUsername(webhook.getUser_username());
        CsGitlabWebhook csGitlabWebhook = GitlabWebhookBuilder.build(webhook, instance, ocUser);
        gitlabWebhookService.addCsGitlabWebhook(csGitlabWebhook);
        return csGitlabWebhook;
    }

    protected void consumed(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhook.setIsConsumed(true);
        gitlabWebhookService.updateCsGitlabWebhook(csGitlabWebhook);
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
        return businessTagService.queryOcBusinessTagByUniqueKey(businessTag) != null;
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        GitlabEventConsumeFactory.register(this);
    }
}
