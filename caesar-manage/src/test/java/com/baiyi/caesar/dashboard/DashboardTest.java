package com.baiyi.caesar.dashboard;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.vo.dashboard.DashboardVO;
import com.baiyi.caesar.facade.DashboardFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/11/5 3:28 下午
 * @Version 1.0
 */
public class DashboardTest extends BaseUnit {

    @Resource
    private DashboardFacade dashboardFacade;

    @Test
    void test() {
        DashboardVO.TopCard topCard = dashboardFacade.queryTopCard();
        System.err.println(JSON.toJSON(topCard));
    }
}
