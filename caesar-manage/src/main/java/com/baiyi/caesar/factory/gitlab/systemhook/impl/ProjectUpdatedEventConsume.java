package com.baiyi.caesar.factory.gitlab.systemhook.impl;

import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.baiyi.caesar.factory.gitlab.systemhook.ISystemHookEventConsume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:34 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ProjectUpdatedEventConsume extends BaseSystemHookEventConsume implements ISystemHookEventConsume {

    @Override
    public String getEventKey() {
        return GitlabEventType.PROJECT_UPDATE.getDesc();
    }

    @Override
    public void consumeEvent(GitlabHookVO.SystemHook systemHook) {
        consumeProjectEvent(systemHook);
    }
}
