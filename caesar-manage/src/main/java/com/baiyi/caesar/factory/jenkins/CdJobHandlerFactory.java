package com.baiyi.caesar.factory.jenkins;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/8/27 4:48 下午
 * @Version 1.0
 */
public class CdJobHandlerFactory {

    static Map<String, ICdJobHandler> context = new ConcurrentHashMap<>();

    public static ICdJobHandler getCdJobByKey(String key) {
        return context.get(key);
    }

    public static void register(ICdJobHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
