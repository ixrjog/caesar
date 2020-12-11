package com.baiyi.caesar.factory.jenkins.monitor;

import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationServerGroup;
import com.baiyi.caesar.opscloud.OpscloudMonitor;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/12/10 4:41 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class MonitorHandler {

    public static final int HOST_STATUS_ENABLE = 0;

    public static final int HOST_STATUS_DISABLE = 1;

    @Resource
    private CsApplicationServerGroupService csApplicationServerGroupService;

    @Resource
    private OpscloudMonitor opscloudMonitor;

    @Async
    public void updateHostStatus(CsApplication csApplication, Map<String, String> params, int status) {
        if (!params.containsKey("hostPattern")) return;
        String hostPattern = params.get("hostPattern");
        String serverGroup = acqServerGroupName(csApplication, params, hostPattern);
        if (StringUtils.isEmpty(serverGroup))
            return;
        try {
            opscloudMonitor.massUpdateMonitorHostStatus(serverGroup, hostPattern, status);
        } catch (IOException e) {
            log.error("更新Zabbix主机监控状态错误! applicationId = {}, serverGroup = {}, hostPattern = {}, status = {}", csApplication.getId()
                    , serverGroup, hostPattern, status);
        }
    }

    private String acqServerGroupName(CsApplication csApplication, Map<String, String> params, String hostPattern) {
        if (params.containsKey("serverGroup"))
            return params.get("serverGroup");
        List<CsApplicationServerGroup> groups = csApplicationServerGroupService.queryCsApplicationServerGroupByApplicationId(csApplication.getId());
        for (CsApplicationServerGroup group : groups) {
            String shortName = group.getServerGroupName().replace("group_", "");
            if (hostPattern.startsWith(shortName + "-"))
                return group.getServerGroupName();
        }
        return null;
    }

}
