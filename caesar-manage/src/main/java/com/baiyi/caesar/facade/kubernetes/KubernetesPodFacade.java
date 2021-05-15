package com.baiyi.caesar.facade.kubernetes;

import com.baiyi.caesar.builder.kubernetes.KubernetesPodBuilder;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.packer.server.ServerDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesCluster;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesClusterNamespace;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesPodParam;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesPodVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.kubernetes.handler.KubernetesPodHandler;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.caesar.service.server.OcServerService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/4 3:54 下午
 * @Version 1.0
 */
@Component
public class KubernetesPodFacade extends BaseKubernetesFacade {

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private KubernetesPodHandler kubernetesPodHandler;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerDecorator serverDecorator;

    public BusinessWrapper<List<KubernetesPodVO.Pod>> queryMyKubernetesPod(KubernetesPodParam.QueryParam queryParam) {
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(queryParam.getInstanceId());
        List<OcKubernetesClusterNamespace> namespaces = queryNamespaceByEnvType(ocKubernetesApplicationInstance.getEnvType());
        if (CollectionUtils.isEmpty(namespaces)) return new BusinessWrapper<>(Lists.newArrayList());
        List<Pod> pods = Lists.newArrayList();
        namespaces.forEach(e -> {
            OcKubernetesCluster ocKubernetesCluster = getOcKubernetesCluster(e);
            PodList podList = kubernetesPodHandler.getPodListByLabel(ocKubernetesCluster.getName(), e.getNamespace(), ocKubernetesApplicationInstance.getInstanceName());
            if (podList != null && !CollectionUtils.isEmpty(podList.getItems()))
                pods.addAll(podList.getItems());
        });
        return new BusinessWrapper(convert(pods, ocKubernetesApplicationInstance.getInstanceName()));
    }

    private List<KubernetesPodVO.Pod> convert(List<Pod> pods, String instanceName) {
        return pods.stream().map(e -> convert(e, instanceName)).collect(Collectors.toList());
    }

    private KubernetesPodVO.Pod convert(Pod pod, String instanceName) {
        KubernetesPodVO.Pod podVO = KubernetesPodBuilder.build(pod, instanceName);
        if (!StringUtils.isEmpty(podVO.getHostIP())) {
            OcServer ocServer = ocServerService.queryOcServerByIp(podVO.getHostIP());
            if (ocServer == null) return podVO;
            podVO.setServer(serverDecorator.decorator(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class)));
        }
        return podVO;
    }


    private List<OcKubernetesClusterNamespace> queryNamespaceByEnvType(int evnType) {
        return ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByEnvType(evnType);
    }
}
