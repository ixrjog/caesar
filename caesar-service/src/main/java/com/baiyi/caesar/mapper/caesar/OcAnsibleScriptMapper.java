package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAnsibleScript;
import com.baiyi.caesar.domain.param.ansible.AnsibleScriptParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAnsibleScriptMapper extends Mapper<OcAnsibleScript> {

    List<OcAnsibleScript> queryOcAnsibleScriptByParam(AnsibleScriptParam.PageQuery pageQuery);
}