package com.baiyi.caesar.factory.gitlab.systemhook;

import com.baiyi.caesar.domain.vo.gitlab.GitlabHooksVO;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:12 下午
 * @Version 1.0
 */
public interface ISystemHookEventConsume {

    String getEventKey();

    /**
     * 消费事件
     * @param systemHook
     */
    void consumeEvent(GitlabHooksVO.SystemHook systemHook);

}
