package com.baiyi.caesar.factory.jenkins;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:40 上午
 * @Version 1.0
 */
public class CiJobHandlerFactory {

    static Map<String, ICiJobHandler> context = new ConcurrentHashMap<>();

    public static ICiJobHandler getCiJobByKey(String key) {
        return context.get(key);
    }

    public static void register(ICiJobHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
