package com.baiyi.caesar.caesar;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.vo.caesar.HealthVO;
import com.baiyi.caesar.facade.impl.CaesarInstanceFacadeImpl;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/9/7 2:43 下午
 * @Version 1.0
 */
public class CaesarInstanceTest extends BaseUnit {

    @Resource
    private CaesarInstanceFacadeImpl caesarInstanceFacade;

    @Test
    void testSsJobKeyRule() {
        HealthVO.Health health = caesarInstanceFacade.checkHealth();
        System.err.println(JSON.toJSONString(health));
    }

}