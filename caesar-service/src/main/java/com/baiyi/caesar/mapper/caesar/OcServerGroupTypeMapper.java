package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcServerGroupType;
import com.baiyi.caesar.domain.param.server.ServerGroupTypeParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerGroupTypeMapper extends Mapper<OcServerGroupType> {

    List<OcServerGroupType> queryOcServerGroupTypeByParam(ServerGroupTypeParam.PageQuery pageQuery);
}