package com.baiyi.caesar.service.kubernetes;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesCluster;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesClusterParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:22 下午
 * @Version 1.0
 */
public interface OcKubernetesClusterService {

    List<OcKubernetesCluster> queryAll();

    OcKubernetesCluster queryOcKubernetesClusterById(int id);

    OcKubernetesCluster queryOcKubernetesClusterByName(String name);

    DataTable<OcKubernetesCluster> queryOcKubernetesClusterByParam(KubernetesClusterParam.PageQuery pageQuery);

    void addOcKubernetesCluster(OcKubernetesCluster ocKubernetesCluster);

    void updateOcKubernetesCluster(OcKubernetesCluster ocKubernetesCluster);

    void deleteOcKubernetesClusterById(int id);
}
