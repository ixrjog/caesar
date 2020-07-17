package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudInstanceType;
import com.baiyi.caesar.domain.param.cloud.CloudInstanceTypeParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/23 1:03 下午
 * @Version 1.0
 */
public interface OcCloudInstanceTypeService {
    DataTable<OcCloudInstanceType> fuzzyQueryOcCloudInstanceTypeByParam(CloudInstanceTypeParam.PageQuery pageQuery);

    List<OcCloudInstanceType> queryOcCloudInstanceTypeByType(int cloudType);

    List<Integer> queryCpuCoreGroup(int cloudType);

    void addOcCloudInstanceType(OcCloudInstanceType ocCloudInstanceType);

    void updateOcCloudInstanceType(OcCloudInstanceType ocCloudInstanceType);

    OcCloudInstanceType queryOcCloudInstanceById(int id);

    void deleteOcCloudInstanceById(int id);

    OcCloudInstanceType queryOcCloudInstanceByUniqueKey(OcCloudInstanceType ocCloudInstanceType);
}
