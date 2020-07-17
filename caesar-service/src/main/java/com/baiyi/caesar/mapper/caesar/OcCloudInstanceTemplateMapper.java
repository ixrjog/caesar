package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudInstanceTemplate;
import com.baiyi.caesar.domain.param.cloud.CloudInstanceTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudInstanceTemplateMapper extends Mapper<OcCloudInstanceTemplate> {

    List<OcCloudInstanceTemplate> fuzzyQueryOcCloudInstanceTemplateByParam(CloudInstanceTemplateParam.PageQuery pageQuery);
}