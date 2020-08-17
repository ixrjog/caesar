package com.baiyi.caesar.dingtalk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:14 下午
 * @Version 1.0
 */
public class DingtalkNotifyFactory {

    static Map<String, IDingtalkNotify> context = new ConcurrentHashMap<>();

    public static IDingtalkNotify getDingtalkNotifyByKey(String key) {
        return context.get(key);
    }

    public static void register(IDingtalkNotify bean) {
        context.put(bean.getKey(), bean);
    }
}
