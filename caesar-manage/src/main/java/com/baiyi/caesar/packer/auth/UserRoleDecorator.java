package com.baiyi.caesar.packer.auth;

import com.baiyi.caesar.domain.generator.caesar.OcAuthRole;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.vo.auth.UserRoleVO;
import com.baiyi.caesar.service.auth.OcAuthRoleService;
import com.baiyi.caesar.service.user.OcUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/8 10:27 上午
 * @Version 1.0
 */
@Component
public class UserRoleDecorator {

    @Resource
    private OcUserService userService;

    @Resource
    private OcAuthRoleService authRoleService;

    /**
     * 插入用户角色详情
     *
     * @param userRole
     * @return
     */
    public UserRoleVO.UserRole wrap(UserRoleVO.UserRole userRole) {
        OcUser ocUser = userService.queryOcUserByUsername(userRole.getUsername());
        userRole.setDisplayName(ocUser.getDisplayName());
        OcAuthRole ocAuthRole = authRoleService.queryOcAuthRoleById(userRole.getRoleId());
        userRole.setRoleName(ocAuthRole.getRoleName());
        userRole.setRoleComment(ocAuthRole.getComment());
        return userRole;
    }
}
