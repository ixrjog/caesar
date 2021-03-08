package com.baiyi.caesar.builder.kubernetes;

import com.baiyi.caesar.bo.kubernetes.KubernetesPodBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesPodVO;
import io.fabric8.kubernetes.api.model.Pod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/4 4:20 下午
 * @Version 1.0
 */
public class KubernetesPodBuilder {

    public static KubernetesPodVO.Pod build(Pod pod,String instanceName) {
        List<KubernetesPodVO.Container> containers = pod.getSpec().getContainers().stream().map(e -> KubernetesContainerBuilder.build(pod, e)).collect(Collectors.toList());
        KubernetesPodBO bo = KubernetesPodBO.builder()
                .instanceName(instanceName)
                .name(pod.getMetadata().getName())
                .hostIP(pod.getStatus().getHostIP())
                .podIP(pod.getStatus().getPodIP())
                .phase(pod.getStatus().getPhase())
                .containers(containers)
                .build();
        return covert(bo);
    }

    private static KubernetesPodVO.Pod covert(KubernetesPodBO bo) {
        return BeanCopierUtil.copyProperties(bo, KubernetesPodVO.Pod.class);
    }
}
