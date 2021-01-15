package com.baiyi.caesar.sonar;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.vo.sonar.SonarMeasures;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/12/24 10:27 上午
 * @Version 1.0
 */
public class SonarQubeTest extends BaseUnit {

    @Resource
    private SonarQubeServer sonarQubeServer;

    @Test
    void test(){
        SonarMeasures sonarMeasures = sonarQubeServer.queryMeasuresComponent("DATA-CENTER_data-center-server-dev");
        System.err.println(JSON.toJSON(sonarMeasures));
    }
}
