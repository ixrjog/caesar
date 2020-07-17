package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.param.server.ServerParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:25 下午
 * @Version 1.0
 */
public interface OcServerService {

    OcServer queryOcServerByPrivateIp(String privateIp);

    OcServer queryOcServerByIp(String ip);

    List<OcServer> queryOcServerByServerGroupId(int serverGroupId);

    List<OcServer> queryAllOcServer();

    OcServer queryOcServerById(int id);

    /**
     * 统计服务器组中的服务器数量
     * @param id
     * @return
     */
    int countByServerGroupId(int id);

    int countByEnvType(int envType);

    DataTable<OcServer> queryOcServerByParam(ServerParam.PageQuery pageQuery);

    DataTable<OcServer> fuzzyQueryOcServerByParam(ServerParam.PageQuery pageQuery);

    int countOcServerByServerGroupId(int serverGroupId);

    void addOcServer(OcServer ocServer);

    void updateOcServer(OcServer ocServer);

    void deleteOcServerById(int id);

    int queryOcServerMaxSerialNumber(int serverGroupId);

    int queryOcServerMaxSerialNumber(int serverGroupId, int envType);

}
