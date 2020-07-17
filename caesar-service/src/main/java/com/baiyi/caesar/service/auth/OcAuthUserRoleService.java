package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcAuthUserRole;
import com.baiyi.caesar.domain.param.auth.UserRoleParam;

/**
 * @Author baiyi
 * @Date 2020/2/12 3:21 下午
 * @Version 1.0
 */
public interface OcAuthUserRoleService {

    int countByRoleId(int roleId);

    DataTable<OcAuthUserRole> queryOcAuthUserRoleByParam(UserRoleParam.PageQuery pageQuery);

    void addOcAuthUserRole(OcAuthUserRole ocAuthUserRole);

    void deleteOcAuthUserRoleById(int id);

    OcAuthUserRole queryOcAuthUserRoleById(int id);

    boolean authenticationByUsernameAndResourceName(String username, String resourceName);

}
