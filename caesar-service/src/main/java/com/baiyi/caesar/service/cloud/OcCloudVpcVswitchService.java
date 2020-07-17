package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudVpcVswitch;
import com.baiyi.caesar.domain.param.cloud.CloudVPCVSwitchParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:57 上午
 * @Version 1.0
 */
public interface OcCloudVpcVswitchService {

    void deleteOcCloudVpcVswitchByVpcId(String vpcId);

    DataTable<OcCloudVpcVswitch> queryOcCloudVPCVswitchByParam(CloudVPCVSwitchParam.PageQuery pageQuery);

    List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcId(String vpcId);

    List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcIdAndZoneIds(String vpcId, List<String> zoneIds);

    OcCloudVpcVswitch queryOcCloudVpcVswitchById(int id);

    OcCloudVpcVswitch queryOcCloudVpcVswitchByVswitchId(String vswitchId);

    void deleteOcCloudVpcVswitchById(int id);

    void addOcCloudVpcVswitch(OcCloudVpcVswitch ocCloudVpcVswitch);

    void updateOcCloudVpcVswitch(OcCloudVpcVswitch ocCloudVpcVswitch);
}
