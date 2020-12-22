package com.baiyi.caesar.factory.gitlab.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHooksVO;
import com.baiyi.caesar.factory.gitlab.IGitlabEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/12/22 10:46 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitlabGroupRenamedEventHandler extends BaseGitlabEventHandler implements IGitlabEventHandler {

    @Override
    public String getEventKey() {
        return GitlabEventType.GROUP_RENAME.getDesc();
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void consumeEvent(GitlabHooksVO.SystemHook systemHook) {
        consumeGroupEvent(systemHook);
    }

}
