package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroupType;
import com.baiyi.caesar.domain.param.server.ServerGroupTypeParam;

/**
 * @Author baiyi
 * @Date 2020/2/21 1:12 下午
 * @Version 1.0
 */
public interface OcServerGroupTypeService {

    OcServerGroupType queryOcServerGroupTypeById(Integer id);

    OcServerGroupType queryOcServerGroupTypeByGrpType(Integer grpType);

    OcServerGroupType queryOcServerGroupTypeByName(String name);

    DataTable<OcServerGroupType> queryOcServerGroupTypeByParam(ServerGroupTypeParam.PageQuery pageQuery);

    void addOcServerGroupType(OcServerGroupType ocServerGroupType);

    void updateOcServerGroupType(OcServerGroupType ocServerGroupType);

    void deleteOcServerGroupTypeById(int id);
}
