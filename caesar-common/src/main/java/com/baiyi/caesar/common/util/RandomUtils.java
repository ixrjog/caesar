package com.baiyi.caesar.common.util;

import java.util.Random;

/**
 * @Author baiyi
 * @Date 2020/11/2 9:49 上午
 * @Version 1.0
 */
public class RandomUtils {

    public static long acqRandom(int second) {
        Random random = new Random();
        return random.nextInt(second * 1000);
    }

}
