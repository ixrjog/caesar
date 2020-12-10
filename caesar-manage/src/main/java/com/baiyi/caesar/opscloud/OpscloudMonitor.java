package com.baiyi.caesar.opscloud;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.opscloud.param.MonitorHostParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author baiyi
 * @Date 2020/12/10 3:38 下午
 * @Version 1.0
 */
@Component
public class OpscloudMonitor {

    public static final String MASS_UPDATE_MONITOR_HOST_STATUS_API = "/monitor/host/mass/update/status";

    public BusinessWrapper<Boolean> massUpdateMonitorHostStatus(String serverGroupName, String hostPattern, int status) throws IOException {
        MonitorHostParam.MassUpdateMonitorHostStatus param = new MonitorHostParam.MassUpdateMonitorHostStatus();
        param.setServerGroupName(serverGroupName);
        param.setHostPattern(hostPattern);
        param.setStatus(status);
        JsonNode jsonNode = OpscloudHttpUtils.httpPutExecutor(MASS_UPDATE_MONITOR_HOST_STATUS_API, param);
        Type type = new TypeToken<BusinessWrapper<Boolean>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(jsonNode.toString(), type);
    }
}
