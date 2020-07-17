package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudDb;
import com.baiyi.caesar.domain.param.cloud.CloudDBParam;

/**
 * @Author baiyi
 * @Date 2020/2/29 2:48 下午
 * @Version 1.0
 */
public interface OcCloudDBService {

    DataTable<OcCloudDb> fuzzyQueryOcCloudDBByParam(CloudDBParam.PageQuery pageQuery);

    OcCloudDb queryOcCloudDbByUniqueKey(int cloudDbType, String dbInstanceId);

    void addOcCloudDb(OcCloudDb ocCloudDb);

    void updateOcCloudDb(OcCloudDb ocCloudDb);

    OcCloudDb queryOcCloudDbById(int id);

    void deleteOcCloudDbById(int id);
}
