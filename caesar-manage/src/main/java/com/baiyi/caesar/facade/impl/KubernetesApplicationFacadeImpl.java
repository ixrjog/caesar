package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.kubernetes.KubernetesApplicationDecorator;
import com.baiyi.caesar.decorator.kubernetes.KubernetesApplicationInstanceDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationInstanceParam;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationParam;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesApplicationVO;
import com.baiyi.caesar.facade.KubernetesApplicationFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.kubernetes.KubernetesDeploymentFacade;
import com.baiyi.caesar.facade.kubernetes.KubernetesServiceFacade;
import com.baiyi.caesar.kubernetes.confg.KubernetesConfig;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesDeploymentService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesServiceService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:11 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class KubernetesApplicationFacadeImpl implements KubernetesApplicationFacade {

    @Resource
    private KubernetesConfig kubernetesConfig;

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcKubernetesApplicationService ocKubernetesApplicationService;

    @Resource
    private OcKubernetesApplicationInstanceService ocKubernetesApplicationInstanceService;

    @Resource
    private KubernetesApplicationDecorator kubernetesApplicationDecorator;

    @Resource
    private KubernetesApplicationInstanceDecorator kubernetesApplicationInstanceDecorator;

    @Resource
    private KubernetesDeploymentFacade kubernetesDeploymentFacade;

    @Resource
    private KubernetesServiceFacade kubernetesServiceFacade;

    @Resource
    private OcKubernetesDeploymentService ocKubernetesDeploymentService;

    @Resource
    private OcKubernetesServiceService ocKubernetesServiceService;

    @Resource
    private UserFacade userFacade;

    @Override
    public DataTable<KubernetesApplicationVO.Application> queryMyKubernetesApplicationPage(KubernetesApplicationParam.PageQuery pageQuery) {
        OcUser ocUser = userFacade.getOcUserBySession();
        DataTable<OcKubernetesApplication> table = ocKubernetesApplicationService.queryOcKubernetesApplicationByParam(pageQuery);
        List<KubernetesApplicationVO.Application> page = BeanCopierUtil.copyListProperties(table.getData(), KubernetesApplicationVO.Application.class);
        return new DataTable<>(page.stream().map(e -> kubernetesApplicationDecorator.decorator(e, ocUser)).collect(Collectors.toList()), table.getTotalNum());
    }


    @Override
    public DataTable<KubernetesApplicationVO.Application> queryKubernetesApplicationPage(KubernetesApplicationParam.PageQuery pageQuery) {
        DataTable<OcKubernetesApplication> table = ocKubernetesApplicationService.queryOcKubernetesApplicationByParam(pageQuery);
        List<KubernetesApplicationVO.Application> page = BeanCopierUtil.copyListProperties(table.getData(), KubernetesApplicationVO.Application.class);
        return new DataTable<>(page.stream().map(e -> kubernetesApplicationDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<KubernetesApplicationVO.Instance> queryKubernetesApplicationInstancePage(KubernetesApplicationInstanceParam.PageQuery pageQuery) {
        DataTable<OcKubernetesApplicationInstance> table = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceByParam(pageQuery);
        return new DataTable<>(table.getData().stream().map(e -> kubernetesApplicationInstanceDecorator.decorator(e, 1)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<KubernetesApplicationVO.Instance> queryKubernetesApplicationInstanceById(int id) {
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(id);
        return new BusinessWrapper<>(kubernetesApplicationInstanceDecorator.decorator(ocKubernetesApplicationInstance, 1));
    }

    @Override
    public List<String> queryKubernetesApplicationInstanceLabel(int envType) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(envType);
        if (ocEnv == null) return Lists.newArrayList();
        return kubernetesConfig.getEnvLabelByEnvName(ocEnv.getEnvName());
    }

    @Override
    public BusinessWrapper<Boolean> addKubernetesApplication(KubernetesApplicationVO.Application application) {
        OcKubernetesApplication ocKubernetesApplication = BeanCopierUtil.copyProperties(application, OcKubernetesApplication.class);
        ocKubernetesApplicationService.addOcKubernetesApplication(ocKubernetesApplication);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addKubernetesApplicationInstance(KubernetesApplicationVO.Instance instance) {
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = BeanCopierUtil.copyProperties(instance, OcKubernetesApplicationInstance.class);
        invokeInstanceName(ocKubernetesApplicationInstance);
        ocKubernetesApplicationInstanceService.addOcKubernetesApplicationInstance(ocKubernetesApplicationInstance);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> createKubernetesDeployment(KubernetesApplicationInstanceParam.CreateByTemplate createDeployment) {
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance
                = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(createDeployment.getInstanceId());
        return kubernetesDeploymentFacade.createKubernetesDeployment(ocKubernetesApplicationInstance, createDeployment.getTemplateId());
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesDeploymentById(int id) {
        OcKubernetesDeployment ocKubernetesDeployment = ocKubernetesDeploymentService.queryOcKubernetesDeploymentById(id);
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance
                = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(ocKubernetesDeployment.getInstanceId());
        BusinessWrapper<Boolean> wrapper = kubernetesDeploymentFacade.deleteKubernetesDeployment(ocKubernetesApplicationInstance);
        if (wrapper.isSuccess()) {
            ocKubernetesDeploymentService.deleteOcKubernetesDeploymentById(ocKubernetesDeployment.getId());
        }
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> createKubernetesService(KubernetesApplicationInstanceParam.CreateByTemplate createService) {
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance
                = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(createService.getInstanceId());
        return kubernetesServiceFacade.createKubernetesService(ocKubernetesApplicationInstance, createService.getTemplateId());
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesServiceById(int id) {
        OcKubernetesService ocKubernetesService = ocKubernetesServiceService.queryOcKubernetesServiceById(id);
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance
                = ocKubernetesApplicationInstanceService.queryOcKubernetesApplicationInstanceById(ocKubernetesService.getInstanceId());
        BusinessWrapper<Boolean> wrapper = kubernetesServiceFacade.deleteKubernetesService(ocKubernetesApplicationInstance);
        if (wrapper.isSuccess()) {
            ocKubernetesServiceService.deleteOcKubernetesServiceById(ocKubernetesService.getId());
        }
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> updateKubernetesApplication(KubernetesApplicationVO.Application application) {
        OcKubernetesApplication pre = BeanCopierUtil.copyProperties(application, OcKubernetesApplication.class);
        OcKubernetesApplication ocKubernetesApplication = ocKubernetesApplicationService.queryOcKubernetesApplicationById(application.getId());
        pre.setName(ocKubernetesApplication.getName()); // 名称不能修改
        ocKubernetesApplicationService.updateOcKubernetesApplication(pre);
        return BusinessWrapper.SUCCESS;
    }

    private void invokeInstanceName(OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        OcKubernetesApplication ocKubernetesApplication = ocKubernetesApplicationService.queryOcKubernetesApplicationById(ocKubernetesApplicationInstance.getApplicationId());
        if (ocKubernetesApplication == null) return;
        String instanceName = Joiner.on("-").join(ocKubernetesApplication.getName(), ocKubernetesApplicationInstance.getEnvLabel());
        ocKubernetesApplicationInstance.setInstanceName(instanceName);
    }

    @Override
    public BusinessWrapper<Boolean> updateKubernetesApplicationInstance(KubernetesApplicationVO.Instance instance) {
        OcKubernetesApplicationInstance ocKubernetesApplicationInstance = BeanCopierUtil.copyProperties(instance, OcKubernetesApplicationInstance.class);
        invokeInstanceName(ocKubernetesApplicationInstance);
        ocKubernetesApplicationInstanceService.updateOcKubernetesApplicationInstance(ocKubernetesApplicationInstance);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesApplicationInstanceById(int id) {
        ocKubernetesApplicationInstanceService.deleteOcKubernetesApplicationInstanceById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKubernetesApplicationById(int id) {
        return BusinessWrapper.SUCCESS;
    }
}
