package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudDb;
import com.baiyi.caesar.domain.param.cloud.CloudDBParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudDbMapper extends Mapper<OcCloudDb> {

    List<OcCloudDb> fuzzyQueryOcCloudDbByParam(CloudDBParam.PageQuery pageQuery);
}