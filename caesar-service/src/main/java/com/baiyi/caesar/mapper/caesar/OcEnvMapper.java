package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.param.env.EnvParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcEnvMapper extends Mapper<OcEnv> {

    List<OcEnv> queryOcEnvByParam(EnvParam.PageQuery pageQuery);
}