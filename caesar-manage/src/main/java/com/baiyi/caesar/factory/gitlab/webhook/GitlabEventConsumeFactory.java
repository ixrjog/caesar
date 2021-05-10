package com.baiyi.caesar.factory.gitlab.webhook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:37 上午
 * @Version 1.0
 */
public class GitlabEventConsumeFactory {

    static Map<String, IGitlabEventConsume> context = new ConcurrentHashMap<>();

    public static IGitlabEventConsume getEventConsumeByKey(String key) {
        return context.get(key);
    }

    public static void register(IGitlabEventConsume bean) {
        context.put(bean.getEventKey(), bean);
    }
}
