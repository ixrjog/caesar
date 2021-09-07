package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.util.BeetlUtil;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateBuilder;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateMap;
import com.baiyi.caesar.dingtalk.config.DingtalkConfig;
import com.baiyi.caesar.dingtalk.content.DingtalkContent;
import com.baiyi.caesar.dingtalk.handler.DingtalkHandler;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.baiyi.caesar.facade.DingtalkFacade;
import com.baiyi.caesar.factory.gitlab.webhook.IGitlabEventConsume;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.baiyi.caesar.service.dingtalk.CsDingtalkTemplateService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:48 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class PushEventConsume extends BaseGitlabEventConsume implements IGitlabEventConsume {

    @Override
    public String getEventKey() {
        return GitlabEventType.PUSH.getDesc();
    }

    @Resource
    private DingtalkHandler dingtalkHandler;

    @Resource
    private CsDingtalkTemplateService csDingtalkTemplateService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private CsDingtalkService csDingtalkService;

    @Resource
    private DingtalkConfig dingtalkConfig;

    @Resource
    private StringEncryptor stringEncryptor;

    @Override
    protected void consume(CsGitlabWebhook csGitlabWebhook) {
        log.info("Gitlab Push Event Consume: eventId = {} ", csGitlabWebhook.getId());
        CsGitlabProject csGitlabProject = getProject(csGitlabWebhook);
        if (csGitlabProject == null || !isAuthBuild(csGitlabProject.getId())) {
            consumed(csGitlabWebhook);
            return;
        }
        String branch = GitlabUtil.getBranch(csGitlabWebhook.getRef());
        // master不触发任务
        if (StringUtils.isEmpty(branch) || "master".equals(branch)) {
            consumed(csGitlabWebhook);
            return;
        }
        consumer(csGitlabProject, csGitlabWebhook, branch);
    }

    private void consumer(CsGitlabProject csGitlabProject, CsGitlabWebhook csGitlabWebhook, String branch) {
        List<CsApplicationScmMember> members = applicationScmMemberService.queryCsApplicationScmMemberByScmId(csGitlabProject.getId());
        if (CollectionUtils.isEmpty(members)) {
            consumed(csGitlabWebhook);
            return;
        }
        members.forEach(m -> {
            // 查询对应的job
            List<CsCiJob> ciJobs = ciJobService.queryCsCiJobByScmMemberIdAndBranch(m.getId(), branch);
            if (!CollectionUtils.isEmpty(ciJobs)) {
                ciJobs.forEach(job -> {
                    IBuildJobHandler buildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(job.getJobType());
                    buildJobHandler.build(job, csGitlabWebhook.getUsername());
                    csGitlabWebhook.setIsConsumed(true);
                    csGitlabWebhook.setIsTrigger(true);
                    csGitlabWebhook.setJobKey(job.getJobKey());
                    updateEvent(csGitlabWebhook);
                });
            }
        });
    }

    @Override
    protected void doNotify(GitlabHookVO.Webhook webhook) {
//        if ("ops".equals(webhook.getProject().getNamespace()))
//            return;
        CsDingtalkTemplate csDingtalkTemplate = csDingtalkTemplateService.queryCsDingtalkTemplateByUniqueKey("GITLAB_PUSH_EVENT", 0, 0);
        // 模版变量
        Map<String, Object> contentMap = buildTemplateContent(webhook);
        try {
            CsDingtalk csDingtalk = csDingtalkService.getByName("GitLab推送通知");
            DingtalkContent dingtalkContent = DingtalkContent.builder()
                    .msg(renderTemplate(csDingtalkTemplate, contentMap))
                    .webHook(buildWebHook(csDingtalk))
                    .build();
            dingtalkHandler.doNotify(dingtalkContent);
        } catch (IOException ignored) {
        }
    }

    private Map<String, Object> buildTemplateContent(GitlabHookVO.Webhook webhook) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(webhook.getUser_username());
        String branch = GitlabUtil.getBranch(webhook.getRef());
        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntryByBranch(branch)
                .paramEntryByDisplayName(webhook.getUser_username(), ocUser)
                .paramEntryByCommits(webhook.getCommits())
                .paramEntry("projectName", webhook.getProject().getName())
                .build();
        return templateMap.getTemplate();
    }

    private String buildWebHook(CsDingtalk csDingtalk) {
        String dingtalkToken = stringEncryptor.decrypt(csDingtalk.getDingtalkToken());
        return dingtalkConfig.buildWebHook(dingtalkToken);
    }

    private String renderTemplate(CsDingtalkTemplate csDingtalkTemplate, Map<String, Object> contentMap) throws IOException {
        return BeetlUtil.renderTemplate(csDingtalkTemplate.getNoticeTemplate(), contentMap);
    }


}
