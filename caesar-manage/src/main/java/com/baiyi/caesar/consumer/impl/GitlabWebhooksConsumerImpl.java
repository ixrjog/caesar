package com.baiyi.caesar.consumer.impl;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.consumer.GitlabWebhooksConsumer;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.tag.BusinessTagVO;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.tag.OcBusinessTagService;
import com.baiyi.caesar.service.tag.OcTagService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/22 10:50 上午
 * @Version 1.0
 */
@Component
public class GitlabWebhooksConsumerImpl implements GitlabWebhooksConsumer {

    @Resource
    private CsGitlabWebhookService csGitlabWebhookService;

    @Resource
    private OcTagService ocTagService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private OcBusinessTagService ocBusinessTagService;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsCiJobService csCiJobService;

    public static final String AUTO_BUILD = "AutoBuild";

    @Override
    // @Async(value = ASYNC_POOL_TASK_COMMON)
    public void consumerWebhooks(CsGitlabWebhook csGitlabWebhook) {
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectByUniqueKey(csGitlabWebhook.getInstanceId(), csGitlabWebhook.getProjectId());
        if (csGitlabProject == null || !isAuthBuild(csGitlabProject.getId())) {
            consumedWebhooks(csGitlabWebhook);
            return;
        }
        String branch = GitlabUtil.getBranch(csGitlabWebhook.getRef());
        // master不触发任务
        if (StringUtils.isEmpty(branch) || branch.equals("master")) {
            consumedWebhooks(csGitlabWebhook);
            return;
        }
        consumerWebhooks(csGitlabProject, csGitlabWebhook, branch);
    }

    @Override
    public void consumerWebhooks() {
        CsGitlabWebhook csGitlabWebhook = csGitlabWebhookService.queryOneCsGitlabWebhookByObjectKind(GitlabEventType.PUSH.getDesc());
        consumerWebhooks(csGitlabWebhook);
    }

    private void consumerWebhooks(CsGitlabProject csGitlabProject, CsGitlabWebhook csGitlabWebhook, String branch) {
        List<CsApplicationScmMember> members = csApplicationScmMemberService.queryCsApplicationScmMemberByScmId(csGitlabProject.getId());
        if (CollectionUtils.isEmpty(members)) {
            consumedWebhooks(csGitlabWebhook);
            return;
        }
        members.forEach(m -> {
            // 查询对应的job
            List<CsCiJob> ciJobs = csCiJobService.queryCsCiJobByScmMemberIdAndBranch(m.getId(), branch);
            if (!CollectionUtils.isEmpty(ciJobs)) {
                for (CsCiJob job : ciJobs) {
                    IBuildJobHandler buildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(job.getJobType());
                    buildJobHandler.build(job, csGitlabWebhook.getUsername());
                    csGitlabWebhook.setIsConsumed(true);
                    csGitlabWebhook.setIsTrigger(true);
                    csGitlabWebhook.setJobKey(job.getJobKey());
                    csGitlabWebhookService.updateCsGitlabWebhook(csGitlabWebhook);
                }
            }
        });
    }

    private void consumedWebhooks(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhook.setIsConsumed(true);
        csGitlabWebhookService.updateCsGitlabWebhook(csGitlabWebhook);
    }

    public boolean isAuthBuild(int projectId) {
        OcTag ocTag = ocTagService.queryOcTagByKey(AUTO_BUILD);
        if (ocTag == null)
            return false;
        BusinessTagVO.BusinessTag businessTag = new BusinessTagVO.BusinessTag();
        businessTag.setBusinessType(BusinessType.GITLAB_PROJECT.getType());
        businessTag.setBusinessId(projectId);
        businessTag.setTagId(ocTag.getId());
        return ocBusinessTagService.queryOcBusinessTagByUniqueKey(businessTag) != null;
    }


}
