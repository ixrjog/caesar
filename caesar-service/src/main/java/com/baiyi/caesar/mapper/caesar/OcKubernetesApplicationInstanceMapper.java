package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesApplicationInstanceMapper extends Mapper<OcKubernetesApplicationInstance> {

    List<OcKubernetesApplicationInstance> queryOcKubernetesApplicationInstanceByParam(KubernetesApplicationInstanceParam.PageQuery pageQuery);
}