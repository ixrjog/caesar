package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudVpcSecurityGroup;
import com.baiyi.caesar.domain.param.cloud.CloudVPCSecurityGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudVpcSecurityGroupMapper extends Mapper<OcCloudVpcSecurityGroup> {

    List<OcCloudVpcSecurityGroup> queryOcCloudVPCSecurityGroupByParam(CloudVPCSecurityGroupParam.PageQuery pageQuery);
}