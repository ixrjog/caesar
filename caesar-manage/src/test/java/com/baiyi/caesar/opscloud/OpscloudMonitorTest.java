package com.baiyi.caesar.opscloud;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.BusinessWrapper;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/12/10 3:54 下午
 * @Version 1.0
 */
class OpscloudMonitorTest extends BaseUnit {

    @Resource
    private OpscloudMonitor opscloudMonitor;


    @Test
    void testMassUpdateMonitorHostStatus() {
//        MonitorHostParam.MassUpdateMonitorHostStatus param = new MonitorHostParam.MassUpdateMonitorHostStatus();
//        param.setHostPattern("account-prod");
//        param.setServerGroupName("group_account");
//        param.setStatus(0);
        try {
            BusinessWrapper<Boolean> wrapper = opscloudMonitor.massUpdateMonitorHostStatus("group_account", "account-prod", 0);
            System.err.println(JSON.toJSON(wrapper));
        } catch (Exception e) {

        }

    }

}