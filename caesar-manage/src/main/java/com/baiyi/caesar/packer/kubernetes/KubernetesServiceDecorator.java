package com.baiyi.caesar.packer.kubernetes;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesCluster;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesServicePort;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesClusterVO;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesServiceVO;
import com.baiyi.caesar.facade.kubernetes.BaseKubernetesFacade;
import com.baiyi.caesar.service.kubernetes.OcKubernetesServicePortService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 2:09 下午
 * @Version 1.0
 */
@Component
public class KubernetesServiceDecorator extends BaseKubernetesFacade {

    @Resource
    private OcKubernetesServicePortService ocKubernetesServicePortService;

    public KubernetesServiceVO.Service decorator(KubernetesServiceVO.Service service, Integer extend) {
        if (extend == 1) {
            service.setPorts(getPorts(service.getId()));
            OcKubernetesCluster ocKubernetesCluster = getOcKubernetesClusterByNamespaceId(service.getNamespaceId());
            service.setCluster(BeanCopierUtil.copyProperties(ocKubernetesCluster, KubernetesClusterVO.Cluster.class));
            invokeBaseProperty(service);
        }
        return service;
    }

    private List<KubernetesServiceVO.Port> getPorts(int serviceId) {
        List<OcKubernetesServicePort> portList = ocKubernetesServicePortService.queryOcKubernetesServicePortByServiceId(serviceId);
        if (CollectionUtils.isEmpty(portList)) return Lists.newArrayList();
        return BeanCopierUtil.copyListProperties(portList, KubernetesServiceVO.Port.class);
    }
}
