package com.baiyi.caesar.factory.block;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/1/26 3:51 下午
 * @Version 1.0
 */
public class BlockRuleFactory {

    static Map<String, IBlockRule> context = new ConcurrentHashMap<>();

    public static IBlockRule getBlockRuleByLevel(String key) {
        return context.get(key);
    }

    public static void register(IBlockRule bean) {
        context.put(bean.getLevel(), bean);
    }
}
