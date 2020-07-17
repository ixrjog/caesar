package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudVpc;
import com.baiyi.caesar.domain.param.cloud.CloudVPCParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudVpcMapper extends Mapper<OcCloudVpc> {

    List<OcCloudVpc> fuzzyQueryOcCloudVpcByParam(CloudVPCParam.PageQuery pageQuery);
}