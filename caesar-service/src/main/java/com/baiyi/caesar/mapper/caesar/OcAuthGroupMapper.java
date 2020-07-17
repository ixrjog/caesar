package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAuthGroup;
import com.baiyi.caesar.domain.param.auth.GroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthGroupMapper extends Mapper<OcAuthGroup> {

    List<OcAuthGroup> queryOcAuthGroupByParam(GroupParam.PageQuery pageQuery);
}