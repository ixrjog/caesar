package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.bo.AuthMenuBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.common.util.MenuUtils;
import com.baiyi.caesar.common.util.SessionUtils;
import com.baiyi.caesar.decorator.auth.ResourceDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.auth.GroupParam;
import com.baiyi.caesar.domain.param.auth.ResourceParam;
import com.baiyi.caesar.domain.param.auth.RoleParam;
import com.baiyi.caesar.domain.param.auth.UserRoleParam;
import com.baiyi.caesar.domain.vo.auth.*;
import com.baiyi.caesar.domain.vo.auth.menu.MenuVO;
import com.baiyi.caesar.facade.AuthFacade;
import com.baiyi.caesar.opscloud.OpscloudUserRole;
import com.baiyi.caesar.service.auth.*;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:21 下午
 * @Version 1.0
 */
@Service
public class AuthFacadeImpl implements AuthFacade {

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    @Resource
    private OcAuthResourceService ocAuthResourceService;

    @Resource
    private OcAuthGroupService ocAuthGroupService;

    @Resource
    private OcAuthRoleResourceService ocAuthRoleResourceService;

    @Resource
    private OcAuthUserRoleService ocAuthUserRoleService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private ResourceDecorator resourceDecorator;

    @Resource
    private OcAuthMenuService ocAuthMenuService;

    @Resource
    private OpscloudUserRole opscloudUserRole;

    @Override
    public DataTable<RoleVO.Role> queryRolePage(RoleParam.PageQuery pageQuery) {
        DataTable<OcAuthRole> table = ocAuthRoleService.queryOcAuthRoleByParam(pageQuery);
        List<RoleVO.Role> page = BeanCopierUtils.copyListProperties(table.getData(), RoleVO.Role.class);
        DataTable<RoleVO.Role> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public void addRole(RoleVO.Role role) {
        OcAuthRole ocAuthRole = BeanCopierUtils.copyProperties(role, OcAuthRole.class);
        ocAuthRoleService.addOcAuthRole(ocAuthRole);
    }

    @Override
    public void updateRole(RoleVO.Role role) {
        OcAuthRole ocAuthRole = BeanCopierUtils.copyProperties(role, OcAuthRole.class);
        ocAuthRoleService.updateOcAuthRole(ocAuthRole);
    }

    @Override
    public BusinessWrapper<Boolean> deleteRoleById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(id);
        if (ocAuthRole == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_ROLE_NOT_EXIST);
        int count = ocAuthUserRoleService.countByRoleId(id);
        if (count == 0) {
            ocAuthRoleService.deleteOcAuthRoleById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_ROLE_HAS_USED);
        }
    }

    @Override
    public DataTable<ResourceVO.Resource> queryRoleBindResourcePage(ResourceParam.BindResourcePageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryRoleBindOcAuthResourceByParam(pageQuery);
        List<ResourceVO.Resource> page = BeanCopierUtils.copyListProperties(table.getData(), ResourceVO.Resource.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DataTable<ResourceVO.Resource> queryRoleUnbindResourcePage(ResourceParam.BindResourcePageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryRoleUnbindOcAuthResourceByParam(pageQuery);
        List<ResourceVO.Resource> page = BeanCopierUtils.copyListProperties(table.getData(), ResourceVO.Resource.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public void bindRoleResource(RoleResourceVO.RoleResource roleResource) {
        OcAuthRoleResource ocAuthRoleResource = BeanCopierUtils.copyProperties(roleResource, OcAuthRoleResource.class);
        ocAuthRoleResourceService.addOcAuthRoleResource(ocAuthRoleResource);
    }

    @Override
    public void unbindRoleResource(int ocRoleResourceId) {
        ocAuthRoleResourceService.delOcAuthRoleResourceById(ocRoleResourceId);
    }

    @Override
    public DataTable<ResourceVO.Resource> queryResourcePage(ResourceParam.PageQuery pageQuery) {
        DataTable<OcAuthResource> table = ocAuthResourceService.queryOcAuthResourceByParam(pageQuery);
        List<ResourceVO.Resource> page = BeanCopierUtils.copyListProperties(table.getData(), ResourceVO.Resource.class);
        return new DataTable<>(page.stream().map(e -> resourceDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public void addResource(ResourceVO.Resource resource) {
        OcAuthResource ocAuthResource = BeanCopierUtils.copyProperties(resource, OcAuthResource.class);
        ocAuthResourceService.addOcAuthResource(ocAuthResource);
    }

    @Override
    public void updateResource(ResourceVO.Resource resource) {
        OcAuthResource ocAuthResource = BeanCopierUtils.copyProperties(resource, OcAuthResource.class);
        ocAuthResourceService.updateOcAuthResource(ocAuthResource);
    }

    @Override
    public void updateResourceNeedAuth(ResourceVO.Resource resource) {
        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceById(resource.getId());
        ocAuthResource.setNeedAuth(resource.getNeedAuth());
        ocAuthResourceService.updateOcAuthResource(ocAuthResource);
    }

    @Override
    public BusinessWrapper<Boolean> deleteResourceById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthResource ocAuthResource = ocAuthResourceService.queryOcAuthResourceById(id);
        if (ocAuthResource == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_RESOURCE_NOT_EXIST);
        // 判断role绑定的资源
        int count = ocAuthRoleResourceService.countByResourceId(id);
        if (count == 0) {
            ocAuthResourceService.deleteOcAuthResourceById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_RESOURCE_HAS_USED);
        }
    }

    @Override
    public DataTable<GroupVO.Group> queryGroupPage(GroupParam.PageQuery pageQuery) {
        DataTable<OcAuthGroup> table = ocAuthGroupService.queryOcAuthGroupByParam(pageQuery);
        List<GroupVO.Group> page = BeanCopierUtils.copyListProperties(table.getData(), GroupVO.Group.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public void addGroup(GroupVO.Group group) {
        OcAuthGroup ocAuthGroup = BeanCopierUtils.copyProperties(group, OcAuthGroup.class);
        ocAuthGroupService.addOcAuthGroup(ocAuthGroup);
    }

    @Override
    public void updateGroup(GroupVO.Group group) {
        OcAuthGroup ocAuthGroup = BeanCopierUtils.copyProperties(group, OcAuthGroup.class);
        ocAuthGroupService.updateOcAuthGroup(ocAuthGroup);
    }

    @Override
    public BusinessWrapper<Boolean> deleteGroupById(int id) {
        // 此处要判断是否有用户绑定role
        OcAuthGroup ocAuthGroup = ocAuthGroupService.queryOcAuthGroupById(id);
        if (ocAuthGroup == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_GROUP_NOT_EXIST);
        int count = ocAuthResourceService.countByGroupId(id);
        if (count == 0) {
            ocAuthGroupService.deleteOcAuthGroupById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTH_GROUP_HAS_USED);
        }
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void syncUserRole() {
        List<OcUser> users = ocUserService.queryOcUserActive();
        users.forEach(u -> {
            try {
                List<UserRoleVO.UserRole> userRoles = opscloudUserRole.queryUserRoles(u.getUsername());
                if (!CollectionUtils.isEmpty(userRoles))
                    userRoles.forEach(r -> {
                        UserRoleVO.UserRole userRole = new UserRoleVO.UserRole();
                        userRole.setUsername(u.getUsername());
                        userRole.setRoleName(r.getRoleName());
                        addUserRole(userRole);
                    });
            } catch (IOException e) {
            }
        });
    }

    @Override
    public DataTable<UserRoleVO.UserRole> queryUserRolePage(UserRoleParam.PageQuery pageQuery) {
        DataTable<OcAuthUserRole> table = ocAuthUserRoleService.queryOcAuthUserRoleByParam(pageQuery);
        List<UserRoleVO.UserRole> page = BeanCopierUtils.copyListProperties(table.getData(), UserRoleVO.UserRole.class);
        return new DataTable<>(page.stream().map(e -> invokeOcUser(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    /**
     * 插入用户信息
     *
     * @param userRole
     * @return
     */
    private UserRoleVO.UserRole invokeOcUser(UserRoleVO.UserRole userRole) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(userRole.getUsername());
        userRole.setDisplayName(ocUser.getDisplayName());
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(userRole.getRoleId());
        userRole.setRoleName(ocAuthRole.getRoleName());
        userRole.setRoleComment(ocAuthRole.getComment());
        return userRole;
    }

    @Override
    public void addUserRole(UserRoleVO.UserRole userRole) {
        try {
            OcAuthUserRole ocAuthUserRole = BeanCopierUtils.copyProperties(userRole, OcAuthUserRole.class);
            // 按角色名插入id
            if (ocAuthUserRole.getRoleId() == null && !StringUtils.isEmpty(userRole.getRoleName())) {
                OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleByName(userRole.getRoleName());
                if (ocAuthRole == null) return;
                ocAuthUserRole.setRoleId(ocAuthRole.getId());
            }
            if (ocAuthUserRoleService.queryOcAuthUserRoleByUniqueKey(ocAuthUserRole) == null)
                ocAuthUserRoleService.addOcAuthUserRole(ocAuthUserRole);
        } catch (Exception ignored) {
        }
    }

    @Override
    public BusinessWrapper<Boolean> deleteUserRoleById(int id) {
        OcAuthUserRole ocAuthUserRole = ocAuthUserRoleService.queryOcAuthUserRoleById(id);
        if (ocAuthUserRole == null)
            return new BusinessWrapper<>(ErrorEnum.AUTH_USER_ROLE_NOT_EXIST);
        ocAuthUserRoleService.deleteOcAuthUserRoleById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> authenticationByResourceName(String resourceName) {
        String username = SessionUtils.getUsername();
        if (ocAuthUserRoleService.authenticationByUsernameAndResourceName(username, resourceName))
            return BusinessWrapper.SUCCESS;
        return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
    }

    @Override
    public List<MenuVO> queryUserMenu() {
        List<MenuVO> menu = Lists.newArrayList();
        OcAuthRole ocAuthRole = ocAuthRoleService.queryTopOcAuthRoleByUsername(SessionUtils.getUsername());
        if (ocAuthRole == null)
            return menu;
        OcAuthMenu ocAuthMenu = ocAuthMenuService.queryOcAuthMenuByRoleId(ocAuthRole.getId(), 0);
        if (ocAuthMenu == null)
            return menu;
        return MenuUtils.buildMenu(ocAuthMenu);
    }

    @Override
    public BusinessWrapper<Boolean> saveRoleMenu(AuthMenuVO.Menu menu) {
        OcAuthMenu ocAuthMenu = BeanCopierUtils.copyProperties(menu, OcAuthMenu.class);
        if (IDUtils.isEmpty(menu.getId())) {
            ocAuthMenuService.addOcAuthMenu(ocAuthMenu);
        } else {
            ocAuthMenuService.updateOcAuthMenu(ocAuthMenu);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public AuthMenuVO.Menu queryRoleMenuByRoleId(int roleId) {
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(roleId);
        OcAuthMenu ocAuthMenu = ocAuthMenuService.queryOcAuthMenuByRoleId(roleId, 0);
        if (ocAuthMenu == null) {
            AuthMenuBO authMenuBO = AuthMenuBO.builder()
                    .roleId(roleId)
                    .comment(ocAuthRole.getRoleName())
                    .build();
            ocAuthMenu = BeanCopierUtils.copyProperties(authMenuBO, OcAuthMenu.class);
            ocAuthMenuService.addOcAuthMenu(ocAuthMenu);
        }
        AuthMenuVO.Menu menu = BeanCopierUtils.copyProperties(ocAuthMenu, AuthMenuVO.Menu.class);
        menu.setRoleName(ocAuthRole.getRoleName());
        return menu;
    }

}
