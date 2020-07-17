package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudVpcVswitch;
import com.baiyi.caesar.domain.param.cloud.CloudVPCVSwitchParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudVpcVswitchMapper extends Mapper<OcCloudVpcVswitch> {

    List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcIdAndZoneIds(@Param("vpcId") String vpcId, @Param("zoneIds") List<String> zoneIds);

    List<OcCloudVpcVswitch> queryOcCloudVPCSecurityGroupByParam(CloudVPCVSwitchParam.PageQuery pageQuery);
}