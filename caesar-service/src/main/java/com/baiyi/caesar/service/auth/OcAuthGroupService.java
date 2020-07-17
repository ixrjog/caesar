package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcAuthGroup;
import com.baiyi.caesar.domain.param.auth.GroupParam;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:07 下午
 * @Version 1.0
 */
public interface OcAuthGroupService {

    OcAuthGroup queryOcAuthGroupById(int id);

    DataTable<OcAuthGroup> queryOcAuthGroupByParam(GroupParam.PageQuery pageQuery);

    void addOcAuthGroup(OcAuthGroup ocAuthGroup);

    void updateOcAuthGroup(OcAuthGroup ocAuthGroup);

    void deleteOcAuthGroupById(int id);

}
