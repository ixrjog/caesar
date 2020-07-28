package com.baiyi.caesar.facade.kubernetes;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.kubernetes.KubernetesApplicationInstanceDecorator;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplication;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesCluster;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesClusterNamespace;
import com.baiyi.caesar.domain.vo.kubernetes.BaseKubernetesApplicationVO;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesApplicationVO;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesClusterService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/1 11:01 上午
 * @Version 1.0
 */
@Component
public class BaseKubernetesFacade {

    @Resource
    private OcKubernetesClusterService ocKubernetesClusterService;

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private OcKubernetesApplicationService ocKubernetesApplicationService;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private KubernetesApplicationInstanceDecorator kubernetesApplicationInstanceDecorator;

    protected OcKubernetesCluster getOcKubernetesCluster(OcKubernetesClusterNamespace ocKubernetesClusterNamespace) {
        return ocKubernetesClusterService.queryOcKubernetesClusterById(ocKubernetesClusterNamespace.getClusterId());
    }

    protected OcKubernetesCluster getOcKubernetesClusterByNamespaceId(int namespaceId) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceById(namespaceId);
        return ocKubernetesClusterService.queryOcKubernetesClusterById(ocKubernetesClusterNamespace.getClusterId());
    }


    protected void invokeBaseProperty(BaseKubernetesApplicationVO.BaseProperty baseProperty) {
        if (baseProperty.getApplicationId() != 0) {
            OcKubernetesApplication ocKubernetesApplication = ocKubernetesApplicationService.queryOcKubernetesApplicationById(baseProperty.getApplicationId());
            baseProperty.setApplication(BeanCopierUtils.copyProperties(ocKubernetesApplication, KubernetesApplicationVO.Application.class));
        }
        if (baseProperty.getInstanceId() != 0) {
            OcKubernetesApplicationInstance ocKubernetesApplicationInstance = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(baseProperty.getInstanceId());
            baseProperty.setInstance(kubernetesApplicationInstanceDecorator.decorator(ocKubernetesApplicationInstance, 0));
        }
    }

}
