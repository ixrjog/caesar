package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.factory.gitlab.webhook.IGitlabEventConsume;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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

}
