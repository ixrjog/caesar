package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsAliyunCrInstance;
import com.baiyi.caesar.domain.param.aliyun.CrParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsAliyunCrInstanceMapper extends Mapper<CsAliyunCrInstance> {
    List<CsAliyunCrInstance> csAliyunCrInstancePageQuery(CrParam.InstancePageQuery pageQuery);
}