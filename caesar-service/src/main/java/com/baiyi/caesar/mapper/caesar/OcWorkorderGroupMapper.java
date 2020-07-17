package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcWorkorderGroup;
import com.baiyi.caesar.domain.param.workorder.WorkorderGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcWorkorderGroupMapper extends Mapper<OcWorkorderGroup> {

    List<OcWorkorderGroup> queryOcWorkorderGroupByParam(WorkorderGroupParam.PageQuery pageQuery);
}