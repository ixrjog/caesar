package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.UserPermissionBO;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.user.UserBusinessGroupParam;
import com.baiyi.caesar.factory.ticket.entry.ServerGroupEntry;

/**
 * @Author baiyi
 * @Date 2020/3/6 11:33 上午
 * @Version 1.0
 */
public class UserPermissionBuilder {

    public static OcUserPermission build(ServerGroupParam.UserServerGroupPermission userServerGroupPermission) {
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userServerGroupPermission.getUserId())
                .businessId(userServerGroupPermission.getServerGroupId())
                .businessType(BusinessType.SERVERGROUP.getType())
                .build();
        return covert(userPermissionBO);
    }

    public static OcUserPermission build(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission) {
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userUserGroupPermission.getUserId())
                .businessId(userUserGroupPermission.getUserGroupId())
                .businessType(BusinessType.USERGROUP.getType())
                .build();
        return covert(userPermissionBO);
    }

    public static OcUserPermission build(OcUser ocUser, ServerGroupEntry serverGroupEntry) {
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(ocUser.getId())
                .businessId(serverGroupEntry.getServerGroup().getId())
                .businessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType())
                .build();
        return covert(userPermissionBO);
    }

    private static OcUserPermission covert(UserPermissionBO userPermissionBO) {
        return BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class);
    }

}
