package com.baiyi.caesar.service.kubernetes;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationInstanceParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:26 下午
 * @Version 1.0
 */
public interface OcKubernetesApplicationInstanceService {

    DataTable<OcKubernetesApplicationInstance> queryOcKubernetesApplicationInstanceByParam(KubernetesApplicationInstanceParam.PageQuery pageQuery);

    List<OcKubernetesApplicationInstance> queryOcKubernetesApplicationInstanceByApplicationId(int applicationId);

    OcKubernetesApplicationInstance queryOcKubernetesApplicationInstanceByInstanceName(String instanceName);

    OcKubernetesApplicationInstance queryOcKubernetesApplicationInstanceById(int id);

    void addOcKubernetesApplicationInstance(OcKubernetesApplicationInstance ocKubernetesApplicationInstance);

    void updateOcKubernetesApplicationInstance(OcKubernetesApplicationInstance ocKubernetesApplicationInstance);

    void deleteOcKubernetesApplicationInstanceById(int id);
}
