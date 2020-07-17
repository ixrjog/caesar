package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartment;
import com.baiyi.caesar.domain.param.org.DepartmentParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcOrgDepartmentMapper extends Mapper<OcOrgDepartment> {

    List<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery);
}