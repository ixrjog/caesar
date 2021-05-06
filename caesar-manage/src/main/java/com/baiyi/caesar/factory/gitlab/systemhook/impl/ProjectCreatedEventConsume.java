package com.baiyi.caesar.factory.gitlab.systemhook.impl;

import com.baiyi.caesar.builder.gitlab.GitlabProjectBuilder;
import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHooksVO;
import com.baiyi.caesar.factory.gitlab.systemhook.ISystemHookEventConsume;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:27 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ProjectCreatedEventConsume extends BaseSystemHookEventConsume implements ISystemHookEventConsume {

    @Override
    public String getEventKey() {
        return GitlabEventType.PROJECT_CREATE.getDesc();
    }

    @Override
    public void consumeEvent(GitlabHooksVO.SystemHook systemHook) {
       consumeProjectEvent(systemHook);
    }

    @Override
    protected void updateProjectEvents(CsGitlabInstance csGitlabInstance, GitlabProject gitlabProject) {
        CsGitlabProject csGitlabProject = GitlabProjectBuilder.build(csGitlabInstance, gitlabProject);
        try {
            csGitlabProjectService.addCsGitlabProject(csGitlabProject);
        } catch (Exception e) {
            log.error("新增Gitlab项目错误，instance = {} , projectId = {}", csGitlabInstance.getName(), gitlabProject.getId(),e);
        }
    }

}
