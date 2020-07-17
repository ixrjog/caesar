package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesService;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesServiceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesServiceMapper extends Mapper<OcKubernetesService> {

    List<OcKubernetesService> queryOcKubernetesServiceByParam(KubernetesServiceParam.PageQuery pageQuery);
}