package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAuthResource;
import com.baiyi.caesar.domain.param.auth.ResourceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthResourceMapper extends Mapper<OcAuthResource> {

    List<OcAuthResource> queryOcAuthResourceByParam(ResourceParam.PageQuery pageQuery);

    List<OcAuthResource> queryOcAuthRoleBindResourceByParam(ResourceParam.BindResourcePageQuery pageQuery);

    List<OcAuthResource> queryOcAuthRoleUnbindResourceByParam(ResourceParam.BindResourcePageQuery pageQuery);

}