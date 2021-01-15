package com.baiyi.caesar.factory.jenkins;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/8/27 4:48 下午
 * @Version 1.0
 */
public class DeploymentJobHandlerFactory {

    static Map<String, IDeploymentJobHandler> context = new ConcurrentHashMap<>();

    public static IDeploymentJobHandler getDeploymentJobByKey(String key) {
        return context.get(key);
    }

    public static void register(IDeploymentJobHandler bean) {
        context.put(bean.getKey(), bean);
    }
}
