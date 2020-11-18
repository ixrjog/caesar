package com.baiyi.caesar.facade.jenkins.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/11/18 11:31 上午
 * @Version 1.0
 */
public class JobEngineFactory {

    static Map<Integer, IJobEngine> context = new ConcurrentHashMap<>();

    public static IJobEngine getJobEngineByKey(Integer key) {
        return context.get(key);
    }

    public static void register(IJobEngine bean) {
        context.put(bean.getKey(), bean);
    }
}
