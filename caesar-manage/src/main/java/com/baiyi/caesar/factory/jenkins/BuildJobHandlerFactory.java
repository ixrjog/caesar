package com.baiyi.caesar.factory.jenkins;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:40 上午
 * @Version 1.0
 */
public class BuildJobHandlerFactory {

    static Map<String, IBuildJobHandler> context = new ConcurrentHashMap<>();

    public static IBuildJobHandler getBuildJobByKey(String key) {
        return context.get(key);
    }

    public static void register(IBuildJobHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
