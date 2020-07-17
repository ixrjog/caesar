package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudImage;
import com.baiyi.caesar.domain.param.cloud.CloudImageParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudImageMapper extends Mapper<OcCloudImage> {

    List<OcCloudImage> fuzzyQueryOcCloudImageByParam(CloudImageParam.PageQuery pageQuery);
}