package com.baiyi.caesar.decorator.auth;

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
@Component("UserRoleDecorator")
public class UserRoleDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    /**
     * 插入用户角色详情
     *
     * @param userRole
     * @return
     */
    public UserRoleVO.UserRole decorator(UserRoleVO.UserRole userRole) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(userRole.getUsername());
        userRole.setDisplayName(ocUser.getDisplayName());
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(userRole.getRoleId());
        userRole.setRoleName(ocAuthRole.getRoleName());
        userRole.setRoleComment(ocAuthRole.getComment());
        return userRole;
    }
}
