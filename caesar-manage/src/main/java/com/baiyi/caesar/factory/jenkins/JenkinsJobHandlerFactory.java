package com.baiyi.caesar.factory.jenkins;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:40 上午
 * @Version 1.0
 */
public class JenkinsJobHandlerFactory {

    static Map<String, IJenkinsJobHandler> context = new ConcurrentHashMap<>();

    public static IJenkinsJobHandler getJenkinsJobBuildByKey(String key) {
        return context.get(key);
    }

    public static void register(IJenkinsJobHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
