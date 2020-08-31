package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.param.application.CdJobParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsCdJobMapper extends Mapper<CsCdJob> {

    List<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobPageQuery pageQuery);
}