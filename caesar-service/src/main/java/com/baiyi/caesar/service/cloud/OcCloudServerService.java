package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudServer;
import com.baiyi.caesar.domain.param.cloud.CloudServerParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:15 上午
 * @Version 1.0
 */
public interface OcCloudServerService {

    DataTable<OcCloudServer> queryOcCloudServerByParam(CloudServerParam.PageQuery pageQuery);

    List<OcCloudServer> queryOcCloudServerByType(int cloudserverType);

    OcCloudServer queryOcCloudServerByInstanceId(String instanceId);

    OcCloudServer queryOcCloudServerByUnqueKey(int cloudServerType,int serverId);

    OcCloudServer queryOcCloudServerById(int id);

    void addOcCloudServer(OcCloudServer ocCloudserver);

    void updateOcCloudServer(OcCloudServer ocCloudserver);

    void deleteOcCloudServerById(int id);
}
