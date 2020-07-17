package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudVpc;
import com.baiyi.caesar.domain.param.cloud.CloudVPCParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:15 下午
 * @Version 1.0
 */
public interface OcCloudVpcService {

    DataTable<OcCloudVpc> fuzzyQueryOcCloudVpcByParam(CloudVPCParam.PageQuery pageQuery);

    List<OcCloudVpc> queryOcCloudVpcByType(int cloudType);

    OcCloudVpc queryOcCloudVpcById(int id);

    OcCloudVpc queryOcCloudVpcByVpcId(String vpcId);

    void deleteOcCloudVpcById(int id);

    void addOcCloudVpc(OcCloudVpc ocCloudVpc);

    void updateOcCloudVpc(OcCloudVpc ocCloudVpc);
}
