package com.baiyi.caesar.factory.gitlab.systemhook;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:22 下午
 * @Version 1.0
 */
public class SystemHookEventConsumeFactory {

    static Map<String, ISystemHookEventConsume> context = new ConcurrentHashMap<>();

    public static ISystemHookEventConsume getSystemHookEventConsumeByKey(String key) {
        return context.get(key);
    }

    public static void register(ISystemHookEventConsume bean) {
        context.put(bean.getEventKey(), bean);
    }
}
