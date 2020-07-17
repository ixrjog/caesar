package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesClusterNamespace;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesClusterNamespaceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesClusterNamespaceMapper extends Mapper<OcKubernetesClusterNamespace> {

    List<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByParam(KubernetesClusterNamespaceParam.PageQuery pageQuery);
}