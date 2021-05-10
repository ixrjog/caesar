package com.baiyi.caesar.factory.gitlab.systemhook.impl;

import com.baiyi.caesar.builder.gitlab.GitlabGroupBuilder;
import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.baiyi.caesar.factory.gitlab.systemhook.ISystemHookEventConsume;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.models.GitlabGroup;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/12/22 11:31 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class GroupCreatedEventConsume extends BaseSystemHookEventConsume implements ISystemHookEventConsume {

    @Override
    public String getEventKey() {
        return GitlabEventType.GROUP_CREATE.getDesc();
    }

    @Override
    public void consumeEvent(GitlabHookVO.SystemHook systemHook) {
        consumeGroupEvent(systemHook);
    }

    @Override
    protected void updateGroupEvents(CsGitlabInstance csGitlabInstance, GitlabGroup gitlabGroup) {
        CsGitlabGroup csGitlabGroup = GitlabGroupBuilder.build(csGitlabInstance, gitlabGroup);
        try {
            csGitlabGroupService.addCsGitlabGroup(csGitlabGroup);
        } catch (Exception e) {
            log.error("新增Gitlab群组错误，instance = {} , groupId = {}", csGitlabInstance.getName(), gitlabGroup.getId(), e);
        }
    }

}
