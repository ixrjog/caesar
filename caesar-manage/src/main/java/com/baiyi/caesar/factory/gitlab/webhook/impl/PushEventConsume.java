package com.baiyi.caesar.factory.gitlab.webhook.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.factory.gitlab.webhook.IWebhookEventConsume;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:48 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class PushEventConsume extends BaseWebhookEventConsume implements IWebhookEventConsume {

    @Override
    public String getEventKey() {
        return GitlabEventType.PUSH.getDesc();
    }

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Override
    public void consume(CsGitlabWebhook csGitlabWebhook) {
        CsGitlabProject csGitlabProject = getProject(csGitlabWebhook);
        if (csGitlabProject == null || !isAuthBuild(csGitlabProject.getId())) {
            consumed(csGitlabWebhook);
            return;
        }
        String branch = GitlabUtil.getBranch(csGitlabWebhook.getRef());
        // master不触发任务
        if (StringUtils.isEmpty(branch) || branch.equals("master")) {
            consumed(csGitlabWebhook);
            return;
        }
        consumer(csGitlabProject, csGitlabWebhook, branch);
    }

    private void consumer(CsGitlabProject csGitlabProject, CsGitlabWebhook csGitlabWebhook, String branch) {
        List<CsApplicationScmMember> members = csApplicationScmMemberService.queryCsApplicationScmMemberByScmId(csGitlabProject.getId());
        if (CollectionUtils.isEmpty(members)) {
            consumed(csGitlabWebhook);
            return;
        }
        members.forEach(m -> {
            // 查询对应的job
            List<CsCiJob> ciJobs = csCiJobService.queryCsCiJobByScmMemberIdAndBranch(m.getId(), branch);
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
