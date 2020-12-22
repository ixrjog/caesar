package com.baiyi.caesar.factory.gitlab.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHooksVO;
import com.baiyi.caesar.factory.gitlab.IGitlabEventHandler;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:33 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectTransferredEventHandler extends BaseGitlabEventHandler implements IGitlabEventHandler {

    @Override
    public String getEventKey() {
        return GitlabEventType.PROJECT_TRANSFER.getDesc();
    }

    @Override
    public void consumeEvent(GitlabHooksVO.SystemHook systemHook) {
        consumeProjectEvent(systemHook);
    }

}
