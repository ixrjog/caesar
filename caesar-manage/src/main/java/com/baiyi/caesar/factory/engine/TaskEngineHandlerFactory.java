package com.baiyi.caesar.factory.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/11/20 9:50 上午
 * @Version 1.0
 */
public class TaskEngineHandlerFactory {

    static Map<Integer, ITaskEngineHandler> context = new ConcurrentHashMap<>();

    public static ITaskEngineHandler getIJobEngineHandlerByKey(Integer key) {
        return context.get(key);
    }

    public static void register(ITaskEngineHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
