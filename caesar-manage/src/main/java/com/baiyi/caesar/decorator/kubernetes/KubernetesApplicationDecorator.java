package com.baiyi.caesar.decorator.kubernetes;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesApplicationVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/1 7:17 下午
 * @Version 1.0
 */
@Component
public class KubernetesApplicationDecorator {

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private KubernetesApplicationInstanceDecorator kubernetesApplicationInstanceDecorator;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    public KubernetesApplicationVO.Application decorator(KubernetesApplicationVO.Application application, OcUser ocUser) {
        application = decorator(application, 1);
        if (application.getServerGroup() == null) {
            application.setAuthorized(false);
        } else {
            application.setAuthorized(userPermissionFacade
                    .tryUserBusinessPermission(ocUser.getId(), BusinessType.SERVERGROUP.getType(), application.getServerGroupId()));
        }
        return application;
    }

    public KubernetesApplicationVO.Application decorator(KubernetesApplicationVO.Application application, Integer extend) {
        if (extend == 1) {
            application.setInstances(getInstances(application.getId()));
            application.setServerGroup(getServerGroup(application.getServerGroupId()));
        }
        return application;
    }

    private ServerGroupVO.ServerGroup getServerGroup(Integer serverGroupId) {
        if (IDUtils.isEmpty(serverGroupId)) return null;
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(serverGroupId);
        if (ocServerGroup == null) return null;
        return BeanCopierUtils.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class);
    }

    private List<KubernetesApplicationVO.Instance> getInstances(int applicationId) {
        List<OcKubernetesApplicationInstance> instanceList = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceByApplicationId(applicationId);
        if (CollectionUtils.isEmpty(instanceList)) return Lists.newArrayList();
        return instanceList.stream().map(e -> kubernetesApplicationInstanceDecorator.decorator(e, 0)).collect(Collectors.toList());
    }
}
