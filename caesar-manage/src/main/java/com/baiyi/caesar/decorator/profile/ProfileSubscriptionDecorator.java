package com.baiyi.caesar.decorator.profile;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.ansible.AnsiblePlaybookDecorator;
import com.baiyi.caesar.domain.generator.caesar.OcAnsiblePlaybook;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.vo.profile.ProfileSubscriptionVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.facade.ServerBaseFacade;
import com.baiyi.caesar.factory.attribute.impl.AttributeAnsible;
import com.baiyi.caesar.service.ansible.OcAnsiblePlaybookService;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:15 上午
 * @Version 1.0
 */
@Component
public class ProfileSubscriptionDecorator {

    @Resource
    private AttributeAnsible attributeAnsible;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private AnsiblePlaybookDecorator ansiblePlaybookDecorator;

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    public ProfileSubscriptionVO.ProfileSubscription decorator(ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(profileSubscription.getServerGroupId());
        profileSubscription.setServerGroup(BeanCopierUtil.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class));
        Map<String, List<OcServer>> serverMap = attributeAnsible.grouping(ocServerGroup, true);
        profileSubscription.setServers(getServersByHostPattern(serverMap, profileSubscription.getHostPattern()));
        if (!IDUtil.isEmpty(profileSubscription.getScriptId())) {
            OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(profileSubscription.getScriptId());
            profileSubscription.setAnsiblePlaybook(ansiblePlaybookDecorator.decorator(ocAnsiblePlaybook));
        }
        return profileSubscription;
    }

    private List<ServerVO.Server> getServersByHostPattern(Map<String, List<OcServer>> serverMap, String hostPattern) {
        if (serverMap.containsKey(hostPattern))
            return BeanCopierUtil.copyListProperties(serverMap.get(hostPattern), ServerVO.Server.class);
        List<ServerVO.Server> servers = Lists.newArrayList();
        for (String key : serverMap.keySet()) {
            for (OcServer ocServer : serverMap.get(key)) {
                if (ServerBaseFacade.acqServerName(ocServer).equals(hostPattern)) {
                    servers.add(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
                    return servers;
                }
            }
        }
        return servers;
    }

}
