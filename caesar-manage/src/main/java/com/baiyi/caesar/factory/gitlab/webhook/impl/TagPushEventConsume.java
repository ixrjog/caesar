package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.factory.gitlab.webhook.IGitlabEventConsume;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.models.GitlabTag;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/6 4:31 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TagPushEventConsume extends BaseGitlabEventConsume implements IGitlabEventConsume {

    public static final String TAG_REF_PREFIX = "refs/tags/";

    @Resource
    private GitlabBranchHandler gitlabBranchHandler;

    @Override
    public String getEventKey() {
        return GitlabEventType.TAG_PUSH.getDesc();
    }

    @Override
    protected void consume(CsGitlabWebhook csGitlabWebhook) {
        log.info("Gitlab TagPush Event Consume: eventId = {} ", csGitlabWebhook.getId());
        // 判断tag是否存在
        String tag = csGitlabWebhook.getRef().replace(TAG_REF_PREFIX, "");
        CsGitlabInstance gitlabInstance = getGitlabInstanceById(csGitlabWebhook.getInstanceId());
        List<GitlabTag> tags = gitlabBranchHandler.getTags(gitlabInstance.getName(), csGitlabWebhook.getProjectId());
        if (tags.stream().noneMatch(e -> e.getName().equals(tag))) {
            log.info("Gitlab Event Consume Error: eventId = {} , tag = {} 不存在!", csGitlabWebhook.getId(), tag);
            return; // tag不存在
        }
        CsGitlabProject csGitlabProject = gitlabProjectService.queryCsGitlabProjectByUniqueKey(gitlabInstance.getId(), csGitlabWebhook.getProjectId());
        if (csGitlabProject == null) {
            log.info("Gitlab Event Consume Error: eventId = {} , projectId = {} 项目不存在!", csGitlabWebhook.getId(), csGitlabWebhook.getProjectId());
            return;
        }
        List<CsApplicationScmMember> scmMembers = applicationScmMemberService.queryCsApplicationScmMemberByScmId(csGitlabProject.getId());
        if (CollectionUtils.isEmpty(scmMembers)) {
            log.info("Gitlab Event Consume Error: eventId = {} 无可消费目标(scmMember)!", csGitlabWebhook.getId());
            return;
        }
        for (CsApplicationScmMember scmMember : scmMembers) {
            consumer(scmMember, csGitlabWebhook, tag);
        }
    }

    private void consumer(CsApplicationScmMember scmMember, CsGitlabWebhook csGitlabWebhook, String tag) {
        // 查询对应的job
        List<CsCiJob> jobs = csCiJobService.queryCsCiJobByScmMemberId(scmMember.getId());
        if (!CollectionUtils.isEmpty(jobs)) {
            for (CsCiJob job : jobs) {
                log.info("Gitlab Event Consume: eventId = {} , jobId = {}!", csGitlabWebhook.getId(),job.getId());
                IBuildJobHandler buildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(job.getJobType());
                buildJobHandler.build(job, csGitlabWebhook.getUsername(), tag);
                csGitlabWebhook.setIsConsumed(true);
                csGitlabWebhook.setIsTrigger(true);
                csGitlabWebhook.setJobKey(job.getJobKey());
                updateEvent(csGitlabWebhook);
            }
        }
    }
}
