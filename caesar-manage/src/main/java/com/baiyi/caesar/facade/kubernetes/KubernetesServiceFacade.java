package com.baiyi.caesar.facade.kubernetes;

import com.baiyi.caesar.builder.kubernetes.KubernetesServiceBuilder;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.packer.kubernetes.KubernetesServiceDecorator;
import com.baiyi.caesar.packer.kubernetes.KubernetesTemplateDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesServiceParam;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesServiceVO;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesTemplateVO;
import com.baiyi.caesar.kubernetes.confg.KubernetesConfig;
import com.baiyi.caesar.kubernetes.handler.KubernetesServiceHandler;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesServiceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesTemplateService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ServiceList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/1 10:56 上午
 * @Version 1.0
 */
@Service
public class KubernetesServiceFacade extends BaseKubernetesFacade {

    @Resource
    private OcKubernetesClusterNamespaceService ocKubernetesClusterNamespaceService;

    @Resource
    private OcKubernetesServiceService ocKubernetesServiceService;

    @Resource
    private KubernetesServiceHandler kubernetesServiceHandler;

    @Resource
    private KubernetesServicePortFacade kubernetesServicePortFacade;

    @Resource
    private KubernetesServiceDecorator kubernetesServiceDecorator;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private KubernetesConfig kubernetesConfig;

    @Resource
    private OcKubernetesTemplateService ocKubernetesTemplateService;

    @Resource
    private KubernetesTemplateDecorator kubernetesTemplateDecorator;

    public DataTable<KubernetesServiceVO.Service> queryKubernetesServicePage(KubernetesServiceParam.PageQuery pageQuery) {
        DataTable<OcKubernetesService> table = ocKubernetesServiceService.queryOcKubernetesServiceByParam(pageQuery);
        List<KubernetesServiceVO.Service> page = BeanCopierUtil.copyListProperties(table.getData(), KubernetesServiceVO.Service.class);
        return new DataTable<>(page.stream().map(e -> kubernetesServiceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    public BusinessWrapper<KubernetesServiceVO.Service> queryKubernetesServiceByParam(KubernetesServiceParam.QueryParam queryParam) {
        OcKubernetesService ocKubernetesService = ocKubernetesServiceService.queryOcKubernetesServiceByInstanceId(queryParam.getInstanceId());
        return new BusinessWrapper(kubernetesServiceDecorator.decorator(BeanCopierUtil.copyProperties(ocKubernetesService, KubernetesServiceVO.Service.class), queryParam.getExtend()));
    }

    @Async(value = Global.TaskPools.COMMON)
    public void syncKubernetesService(int namespaceId) {
        OcKubernetesClusterNamespace ocKubernetesClusterNamespace = ocKubernetesClusterNamespaceService.queryOcKubernetesClusterNamespaceById(namespaceId);
        ServiceList serviceList = kubernetesServiceHandler.getServiceList(getOcKubernetesCluster(ocKubernetesClusterNamespace).getName(), ocKubernetesClusterNamespace.getNamespace());
        if (serviceList == null || CollectionUtils.isEmpty(serviceList.getItems())) return;
        Map<String, OcKubernetesService> serviceMap = getServiceMap(namespaceId);
        serviceList.getItems().forEach(e -> saveKubernetesService(serviceMap, ocKubernetesClusterNamespace, e));
        delKubernetesServiceByMap(serviceMap);
    }

    private void delKubernetesServiceByMap(Map<String, OcKubernetesService> serviceMap) {
        if (serviceMap.isEmpty()) return;
        serviceMap.keySet().forEach(k -> ocKubernetesServiceService.deleteOcKubernetesServiceById(serviceMap.get(k).getId()));
    }

    private Map<String, OcKubernetesService> getServiceMap(int namespaceId) {
        List<OcKubernetesService> services = ocKubernetesServiceService.queryOcKubernetesServiceByNamespaceId(namespaceId);
        if (CollectionUtils.isEmpty(services)) return Maps.newHashMap();
        return services.stream().collect(Collectors.toMap(OcKubernetesService::getName, a -> a, (k1, k2) -> k1));
    }

    private void saveKubernetesService(Map<String, OcKubernetesService> serviceMap, OcKubernetesClusterNamespace ocKubernetesClusterNamespace, io.fabric8.kubernetes.api.model.Service service) {
        OcKubernetesService pre = KubernetesServiceBuilder.build(ocKubernetesClusterNamespace, service);
        OcKubernetesService ocKubernetesService = ocKubernetesServiceService.queryOcKubernetesServiceByUniqueKey(ocKubernetesClusterNamespace.getId(), pre.getName());
        if (ocKubernetesService != null)
            pre = ocKubernetesService;
        invokeKubernetesService(pre, service);
        if (IDUtil.isEmpty(pre.getId())) {
            ocKubernetesServiceService.addOcKubernetesService(pre);
        } else {
            ocKubernetesServiceService.updateOcKubernetesService(pre);
        }
        kubernetesServicePortFacade.saveKubernetesServicePort(pre, service);
        serviceMap.remove(pre.getName());
    }

    private void invokeKubernetesService(OcKubernetesService ocKubernetesService, io.fabric8.kubernetes.api.model.Service service) {
        if (!IDUtil.isEmpty(ocKubernetesService.getInstanceId())) return;
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = getApplicationInstanceByService(service);
        if (ocKubernetesApplicationInstance != null) {
            ocKubernetesService.setApplicationId(ocKubernetesApplicationInstance.getApplicationId());
            ocKubernetesService.setInstanceId(ocKubernetesApplicationInstance.getId());
        }
    }

    private OcKubernetesApplicationInstance getApplicationInstanceByService(io.fabric8.kubernetes.api.model.Service service) {
        String instanceName = kubernetesConfig.getApplicationInstanceNameByServiceName(service.getMetadata().getName());
        return getApplicationInstanceByName(instanceName);
    }

    private OcKubernetesApplicationInstance getApplicationInstanceByName(String name) {
        return ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceByInstanceName(name);
    }


    public BusinessWrapper<Boolean> createKubernetesService(OcKubernetesApplicationInstance ocKubernetesApplicationInstance, Integer templateId) {
        OcKubernetesTemplate ocKubernetesTemplate = ocKubernetesTemplateService.queryOcKubernetesTemplateById(templateId);
        KubernetesTemplateVO.Template serviceTemplate = kubernetesTemplateDecorator.decorator(BeanCopierUtil.copyProperties(ocKubernetesTemplate, KubernetesTemplateVO.Template.class), ocKubernetesApplicationInstance);
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
        io.fabric8.kubernetes.api.model.Service service = kubernetesServiceHandler.createOrReplaceService(ocKubernetesCluster.getName(), ocKubernetesClusterNamespace.getNamespace(), serviceTemplate.getTemplateYaml());
        if (service != null) {
            saveKubernetesService(Maps.newHashMap(), ocKubernetesClusterNamespace, service);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper(ErrorEnum.KUBERNETES_CREATE_SERVICE_ERROR);
    }

    public BusinessWrapper<Boolean> deleteKubernetesService(OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
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
        String serviceName = kubernetesConfig.getServiceName(ocKubernetesApplicationInstance.getInstanceName());
        boolean result = kubernetesServiceHandler.deleteService(ocKubernetesCluster.getName(), ocKubernetesClusterNamespace.getNamespace(), serviceName);
        if (result) return BusinessWrapper.SUCCESS;
        return new BusinessWrapper(ErrorEnum.KUBERNETES_CREATE_SERVICE_ERROR);
    }

    public BusinessWrapper<Boolean> deleteKubernetesServiceById(int id) {
        OcKubernetesService ocKubernetesService = ocKubernetesServiceService.queryOcKubernetesServiceById(id);
        if (ocKubernetesService != null) {
            kubernetesServicePortFacade.delKubernetesServicePortByServiceId(id);
            ocKubernetesServiceService.deleteOcKubernetesServiceById(id);
        }
        return BusinessWrapper.SUCCESS;
    }

}
