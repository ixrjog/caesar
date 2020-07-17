package com.baiyi.caesar.service.kubernetes;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplication;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationParam;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:13 下午
 * @Version 1.0
 */
public interface OcKubernetesApplicationService {

    DataTable<OcKubernetesApplication> queryOcKubernetesApplicationByParam(KubernetesApplicationParam.PageQuery pageQuery);

    void addOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication);

    void updateOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication);

    void deleteOcKubernetesApplicationById(int id);

   OcKubernetesApplication queryOcKubernetesApplicationById(int id);
}
