package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAliyunLog;
import com.baiyi.caesar.domain.param.cloud.AliyunLogParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunLogMapper extends Mapper<OcAliyunLog> {

    List<OcAliyunLog> queryOcAliyunLogByParam(AliyunLogParam.PageQuery pageQuery);
}