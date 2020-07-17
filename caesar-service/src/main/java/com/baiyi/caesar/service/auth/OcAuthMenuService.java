package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.generator.caesar.OcAuthMenu;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:27 上午
 * @Version 1.0
 */
public interface OcAuthMenuService {

    OcAuthMenu queryOcAuthMenuByRoleId(int roleId, int menuType);

    void addOcAuthMenu(OcAuthMenu ocAuthMenu);

    void updateOcAuthMenu(OcAuthMenu ocAuthMenu);

    void deletetOcAuthMenuById(OcAuthMenu ocAuthMenu);
}
