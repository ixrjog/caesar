package com.baiyi.caesar.packer.kubernetes;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesClusterNamespace;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesClusterNamespaceVO;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesClusterVO;
import com.baiyi.caesar.service.kubernetes.OcKubernetesClusterNamespaceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/28 4:33 下午
 * @Version 1.0
 */
@Component
public class KubernetesClusterDecorator {

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    public KubernetesClusterVO.Cluster decorator(KubernetesClusterVO.Cluster cluster, Integer extend) {
        if (extend == 1) {
            List<OcKubernetesClusterNamespace> list = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByClusterId(cluster.getId());
            cluster.setNamespaces(BeanCopierUtil.copyListProperties(list, KubernetesClusterNamespaceVO.Namespace.class));
        }
        return cluster;
    }

}
