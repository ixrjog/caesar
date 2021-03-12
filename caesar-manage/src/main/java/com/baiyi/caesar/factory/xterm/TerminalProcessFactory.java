package com.baiyi.caesar.factory.xterm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:50 上午
 * @Version 1.0
 */
public class TerminalProcessFactory {

    static Map<String, ITerminalProcess> context = new ConcurrentHashMap<>();

    public static ITerminalProcess getIXTermProcessByKey(String key) {
        return context.get(key);
    }

    public static void register(ITerminalProcess bean) {
        context.put(bean.getKey(), bean);
    }
}
