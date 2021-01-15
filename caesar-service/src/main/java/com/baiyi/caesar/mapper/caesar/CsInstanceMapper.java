package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsInstance;
import com.baiyi.caesar.domain.param.caesar.CaesarInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsInstanceMapper extends Mapper<CsInstance> {

    List<CsInstance> queryCsInstanceByParam(CaesarInstanceParam.CaesarInstancePageQuery pageQuery);
}