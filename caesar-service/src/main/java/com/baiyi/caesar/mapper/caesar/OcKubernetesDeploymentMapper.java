package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesDeployment;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesDeploymentParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesDeploymentMapper extends Mapper<OcKubernetesDeployment> {

    List<OcKubernetesDeployment> queryOcKubernetesDeploymentByParam(KubernetesDeploymentParam.PageQuery pageQuery);

}