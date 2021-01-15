package com.baiyi.caesar.service;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/6 5:33 下午
 * @Version 1.0
 */
public class CsCiJobBuildServiceTest extends BaseUnit {

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Test
    void test1() {
        List<BuildTaskGroupByHour> list = csCiJobBuildService.queryCiJobBuildGroupByHour();
        System.err.println(JSON.toJSON(list));

    }
}
