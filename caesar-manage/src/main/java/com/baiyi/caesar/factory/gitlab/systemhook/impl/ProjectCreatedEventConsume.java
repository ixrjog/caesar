package com.baiyi.caesar.factory.gitlab.systemhook.impl;

import com.baiyi.caesar.builder.gitlab.GitlabProjectBuilder;
import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.factory.gitlab.systemhook.ISystemHookEventConsume;
import com.baiyi.caesar.service.application.CsApplicationScmGroupService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:27 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ProjectCreatedEventConsume extends BaseSystemHookEventConsume implements ISystemHookEventConsume {

    @Resource
    private CsApplicationScmGroupService csApplicationScmGroupService;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Override
    public String getEventKey() {
        return GitlabEventType.PROJECT_CREATE.getDesc();
    }

    @Override
    public void consumeEvent(GitlabHookVO.SystemHook systemHook) {
        consumeProjectEvent(systemHook);
    }

    /**
     * 增加群组成员同步
     *
     * @param csGitlabInstance
     * @param gitlabProject
     */
    @Override
    protected void updateProjectEvents(CsGitlabInstance csGitlabInstance, GitlabProject gitlabProject) {
        CsGitlabProject csGitlabProject = GitlabProjectBuilder.build(csGitlabInstance, gitlabProject);
        try {
            csGitlabProjectService.addCsGitlabProject(csGitlabProject);
            sync(csGitlabInstance, csGitlabProject);
        } catch (Exception e) {
            log.error("新增Gitlab项目错误，instance = {} , projectId = {}", csGitlabInstance.getName(), gitlabProject.getId(), e);
        }
    }

    private void sync(CsGitlabInstance csGitlabInstance, CsGitlabProject csGitlabProject) {
        CsGitlabGroup gitlabGroup = csGitlabGroupService.queryGitlabGroupByInstanceIdAndPath(csGitlabInstance.getId(), csGitlabProject.getNamespacePath());
        if (gitlabGroup == null) return;
        List<CsApplicationScmGroup> scmGroups = csApplicationScmGroupService.queryApplicationScmGroupByGroupId(gitlabGroup.getGroupId());
        scmGroups.forEach(e -> applicationFacade.syncApplicationScmMember(e.getApplicationId()));
    }

}
