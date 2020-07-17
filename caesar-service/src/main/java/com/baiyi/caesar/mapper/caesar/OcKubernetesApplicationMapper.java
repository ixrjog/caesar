package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplication;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKubernetesApplicationMapper extends Mapper<OcKubernetesApplication> {

    List<OcKubernetesApplication> queryOcKubernetesApplicationByParam(KubernetesApplicationParam.PageQuery pageQuery);
}