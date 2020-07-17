package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartmentMember;
import com.baiyi.caesar.domain.param.org.DepartmentMemberParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcOrgDepartmentMemberMapper extends Mapper<OcOrgDepartmentMember> {

    List<OcOrgDepartmentMember> queryOcOrgDepartmentMemberParam(DepartmentMemberParam.PageQuery pageQuery);
}