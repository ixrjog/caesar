package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/25 5:14 下午
 * @Version 1.0
 */
public interface OcUserPermissionService {

    void addOcUserPermission(OcUserPermission ocUserPermission);

    void updateOcUserPermission(OcUserPermission ocUserPermission);

    void delOcUserPermissionById(int id);

    OcUserPermission queryOcUserPermissionById(int id);

    OcUserPermission queryOcUserPermissionByUniqueKey(OcUserPermission ocUserPermission);

    List<OcUserPermission> queryUserBusinessPermissionByUserId(int userId, int businessId);

    List<OcUserPermission> queryUserBusinessPermissionByBusinessId(int businessType, int businessId);
}
