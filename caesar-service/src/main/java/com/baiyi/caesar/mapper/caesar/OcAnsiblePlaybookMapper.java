package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAnsiblePlaybook;
import com.baiyi.caesar.domain.param.ansible.AnsiblePlaybookParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAnsiblePlaybookMapper extends Mapper<OcAnsiblePlaybook> {

    List<OcAnsiblePlaybook> queryOcAnsiblePlaybookByParam(AnsiblePlaybookParam.PageQuery pageQuery);
}