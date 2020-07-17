package com.baiyi.caesar.builder.kubernetes;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.bo.kubernetes.KubernetesServiceBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesClusterNamespace;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesService;
import io.fabric8.kubernetes.api.model.Service;

/**
 * @Author baiyi
 * @Date 2020/7/1 10:06 上午
 * @Version 1.0
 */
public class KubernetesServiceBuilder {

    public static OcKubernetesService build(OcKubernetesClusterNamespace ocKubernetesClusterNamespace, Service service) {
        KubernetesServiceBO bo = KubernetesServiceBO.builder()
                .namespaceId(ocKubernetesClusterNamespace.getId())
                .namespace(ocKubernetesClusterNamespace.getNamespace())
                .name(service.getMetadata().getName())
                .clusterIp(service.getSpec().getClusterIP())
                .serviceType(service.getSpec().getType())
                .serviceDetail(JSON.toJSONString(service))
                .build();
        return covert(bo);
    }

    private static OcKubernetesService covert(KubernetesServiceBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcKubernetesService.class);
    }
}
