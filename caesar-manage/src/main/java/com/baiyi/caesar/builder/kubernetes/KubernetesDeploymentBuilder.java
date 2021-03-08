package com.baiyi.caesar.builder.kubernetes;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.bo.kubernetes.KubernetesDeploymentBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesClusterNamespace;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesDeployment;
import io.fabric8.kubernetes.api.model.apps.Deployment;


/**
 * @Author baiyi
 * @Date 2020/6/29 2:54 下午
 * @Version 1.0
 */
public class KubernetesDeploymentBuilder {

    public static OcKubernetesDeployment build(OcKubernetesClusterNamespace ocKubernetesClusterNamespace, Deployment deployment) {
        KubernetesDeploymentBO bo = KubernetesDeploymentBO.builder()
                .namespaceId(ocKubernetesClusterNamespace.getId())
                .namespace(ocKubernetesClusterNamespace.getNamespace())
                .name(deployment.getMetadata().getName())
                .labelApp(deployment.getMetadata().getLabels().getOrDefault("app", ""))
                .replicas(deployment.getStatus().getReplicas() == null ? 0 : deployment.getStatus().getReplicas())
                .availableReplicas(deployment.getStatus().getAvailableReplicas() == null ? 0 : deployment.getStatus().getAvailableReplicas())
                .deploymentDetail(JSON.toJSONString(deployment))
                .build();
        return covert(bo);
    }

    private static OcKubernetesDeployment covert(KubernetesDeploymentBO bo) {
        return BeanCopierUtil.copyProperties(bo, OcKubernetesDeployment.class);
    }
}
