package com.baiyi.caesar.factory.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/11/20 9:50 上午
 * @Version 1.0
 */
public class JobEngineHandlerFactory {

    static Map<Integer, IJobEngineHandler> context = new ConcurrentHashMap<>();

    public static IJobEngineHandler getIJobEngineHandlerByKey(Integer key) {
        return context.get(key);
    }

    public static void register(IJobEngineHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
