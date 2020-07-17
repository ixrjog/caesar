package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesCluster;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesClusterParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesClusterMapper extends Mapper<OcKubernetesCluster> {

    List<OcKubernetesCluster> queryOcKubernetesClusterByParam(KubernetesClusterParam.PageQuery pageQuery);
}