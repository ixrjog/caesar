package com.baiyi.caesar.facade.kubernetes;

import com.baiyi.caesar.builder.kubernetes.KubernetesDeploymentBuilder;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.packer.kubernetes.KubernetesDeploymentDecorator;
import com.baiyi.caesar.packer.kubernetes.KubernetesTemplateDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesDeploymentParam;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesDeploymentVO;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesTemplateVO;
import com.baiyi.caesar.kubernetes.confg.KubernetesConfig;
import com.baiyi.caesar.kubernetes.handler.KubernetesDeploymentHandler;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesDeploymentService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesTemplateService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2020/7/1 11:03 上午
 * @Version 1.0
 */
@Service
public class KubernetesDeploymentFacade extends BaseKubernetesFacade {

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private KubernetesDeploymentHandler kubernetesDeploymentHandler;

    @Resource
    private OcKubernetesDeploymentService ocKubernetesDeploymentService;

    @Resource
    private KubernetesDeploymentDecorator kubernetesDeploymentDecorator;

    @Resource
    private KubernetesTemplateDecorator kubernetesTemplateDecorator;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private OcKubernetesTemplateService ocKubernetesTemplateService;

    @Resource
    private KubernetesConfig kubernetesConfig;

    public DataTable<KubernetesDeploymentVO.Deployment> queryKubernetesDeploymentPage(KubernetesDeploymentParam.PageQuery pageQuery) {
        DataTable<OcKubernetesDeployment> table = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByParam(pageQuery);
        List<KubernetesDeploymentVO.Deployment> page = BeanCopierUtil.copyListProperties(table.getData(), KubernetesDeploymentVO.Deployment.class);
        return new DataTable<>(page.stream().map(e -> kubernetesDeploymentDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    public BusinessWrapper<Boolean> createKubernetesDeployment(OcKubernetesApplicationInstance ocKubernetesApplicationInstance, Integer templateId) {
        OcKubernetesTemplate ocKubernetesTemplate = ocKubernetesTemplateService.queryOcKubernetesTemplateById(templateId);
        KubernetesTemplateVO.Template deploymentTemplate = kubernetesTemplateDecorator.decorator(BeanCopierUtil.copyProperties(ocKubernetesTemplate, KubernetesTemplateVO.Template.class), ocKubernetesApplicationInstance);
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace;
        try {
            ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByEnvType(ocKubernetesApplicationInstance.getEnvType()).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper(ErrorEnum.KUBERNETES_NAMESPACE_NOT_EXIST);
        }
        OcKubernetesCluster ocKubernetesCluster = getOcKubernetesCluster(ocKubernetesClusterNamespace);
        if (ocKubernetesCluster == null)
            return new BusinessWrapper(ErrorEnum.KUBERNETES_CLUSTER_NOT_EXIST);
        Deployment deployment = kubernetesDeploymentHandler.createOrReplaceDeployment(ocKubernetesCluster.getName(), ocKubernetesClusterNamespace.getNamespace(), deploymentTemplate.getTemplateYaml());
        if (deployment != null) {
            saveKubernetesDeployment(Maps.newHashMap(), ocKubernetesClusterNamespace, deployment);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper(ErrorEnum.KUBERNETES_CREATE_DEPLOYMENT_ERROR);
    }

    public BusinessWrapper<Boolean> deleteKubernetesDeployment(OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace;
        try {
            ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceByEnvType(ocKubernetesApplicationInstance.getEnvType()).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper(ErrorEnum.KUBERNETES_NAMESPACE_NOT_EXIST);
        }
        OcKubernetesCluster ocKubernetesCluster = getOcKubernetesCluster(ocKubernetesClusterNamespace);
        if (ocKubernetesCluster == null)
            return new BusinessWrapper(ErrorEnum.KUBERNETES_CLUSTER_NOT_EXIST);
        String deploymentName = kubernetesConfig.getDeploymentName(ocKubernetesApplicationInstance.getInstanceName());
        boolean result = kubernetesDeploymentHandler.deleteDeployment(ocKubernetesCluster.getName(), ocKubernetesClusterNamespace.getNamespace(), deploymentName);
        if (result) return BusinessWrapper.SUCCESS;
        return new BusinessWrapper(ErrorEnum.KUBERNETES_CREATE_DEPLOYMENT_ERROR);
    }

    @Async(value = Global.TaskPools.COMMON)
    public void syncKubernetesDeployment(int namespaceId) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceById(namespaceId);
        DeploymentList deploymentList = kubernetesDeploymentHandler.getDeploymentList(getOcKubernetesCluster(ocKubernetesClusterNamespace).getName(), ocKubernetesClusterNamespace.getNamespace());
        if (deploymentList == null || CollectionUtils.isEmpty(deploymentList.getItems())) return;
        Map<String, OcKubernetesDeployment> deploymentMap = getDeploymentMap(namespaceId);
        deploymentList.getItems().forEach(e -> saveKubernetesDeployment(deploymentMap, ocKubernetesClusterNamespace, e));
        delKubernetesDeploymentByMap(deploymentMap);
    }

    private void delKubernetesDeploymentByMap(Map<String, OcKubernetesDeployment> deploymentMap) {
        if (deploymentMap.isEmpty()) return;
        deploymentMap.keySet().forEach(k -> ocKubernetesDeploymentService.deleteOcKubernetesDeploymentById(deploymentMap.get(k).getId()));
    }

    private Map<String, OcKubernetesDeployment> getDeploymentMap(int namespaceId) {
        List<OcKubernetesDeployment> deployments = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByNamespaceId(namespaceId);
        if (CollectionUtils.isEmpty(deployments)) return Maps.newHashMap();
        return deployments.stream().collect(Collectors.toMap(OcKubernetesDeployment::getName, a -> a, (k1, k2) -> k1));
    }

    private void saveKubernetesDeployment(Map<String, OcKubernetesDeployment> deploymentMap, OcKubernetesClusterNamespace ocKubernetesClusterNamespace, Deployment deployment) {
        OcKubernetesDeployment pre = KubernetesDeploymentBuilder.build(ocKubernetesClusterNamespace, deployment);
        OcKubernetesDeployment ocKubernetesDeployment = ocKubernetesDeploymentService.queryOcKubernetesDeploymentByUniqueKey(ocKubernetesClusterNamespace.getId(), pre.getName());
        if (ocKubernetesDeployment == null) {
            // 匹配应用实例
            OcKubernetesApplicationInstance ocKubernetesApplicationInstance = getApplicationInstanceByDeployment(deployment);
            if (ocKubernetesApplicationInstance != null) {
                pre.setApplicationId(ocKubernetesApplicationInstance.getApplicationId());
                pre.setInstanceId(ocKubernetesApplicationInstance.getId());
            }
            ocKubernetesDeploymentService.addOcKubernetesDeployment(pre);
        } else {
            pre.setId(ocKubernetesDeployment.getId());
            ocKubernetesDeploymentService.updateOcKubernetesDeployment(pre);
        }
        deploymentMap.remove(pre.getName());
    }

    private OcKubernetesApplicationInstance getApplicationInstanceByDeployment(Deployment deployment) {
        String instanceName = kubernetesConfig.getApplicationInstanceNameByDeploymentName(deployment.getMetadata().getName());
        return getApplicationInstanceByName(instanceName);
    }

    private OcKubernetesApplicationInstance getApplicationInstanceByName(String name) {
        return ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceByInstanceName(name);
    }
}
