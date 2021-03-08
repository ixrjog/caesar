package com.baiyi.caesar.common;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.RegexUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/9/7 9:35 上午
 * @Version 1.0
 */
public class UtilsTest extends BaseUnit {

    @Test
    void testSsJobKeyRule(){

       boolean r= RegexUtil.isJobKeyRule("aaa-ddd-A");
       System.err.println(r);
    }


    @Test
    void test(){

        boolean r= RegexUtil.isApplicationKeyRule("AAA-dd");
        System.err.println(r);
    }
}
