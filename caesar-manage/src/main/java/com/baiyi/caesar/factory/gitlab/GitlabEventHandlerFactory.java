package com.baiyi.caesar.factory.gitlab;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:22 下午
 * @Version 1.0
 */
public class GitlabEventHandlerFactory {

    static Map<String, IGitlabEventHandler> context = new ConcurrentHashMap<>();

    public static IGitlabEventHandler getGitlabEventHanlderByKey(String key) {
        return context.get(key);
    }

    public static void register(IGitlabEventHandler bean) {
        context.put(bean.getEventKey(), bean);
    }
}
