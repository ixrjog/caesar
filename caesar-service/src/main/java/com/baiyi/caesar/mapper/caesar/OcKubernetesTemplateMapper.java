package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesTemplate;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesTemplateMapper extends Mapper<OcKubernetesTemplate> {

    List<OcKubernetesTemplate> queryKubernetesTemplateByParam(KubernetesTemplateParam.PageQuery pageQuery);
}