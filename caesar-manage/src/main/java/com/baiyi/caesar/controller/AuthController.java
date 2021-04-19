package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.auth.GroupParam;
import com.baiyi.caesar.domain.param.auth.ResourceParam;
import com.baiyi.caesar.domain.param.auth.RoleParam;
import com.baiyi.caesar.domain.param.auth.UserRoleParam;
import com.baiyi.caesar.domain.vo.auth.*;
import com.baiyi.caesar.domain.vo.auth.menu.MenuVO;
import com.baiyi.caesar.facade.AuthFacade;
import com.baiyi.caesar.facade.TokenFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/13 8:02 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "权限配置")
public class AuthController {
    @Resource
    private AuthFacade authFacade;

    @Resource
    private TokenFacade tokenFacade;

    @ApiOperation(value = "分页查询role列表")
    @GetMapping(value = "/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RoleVO.Role>> queryRolePage(@Valid RoleParam.PageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRolePage(pageQuery));
    }

    @ApiOperation(value = "新增role")
    @PostMapping(value = "/role/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRole(@RequestBody @Valid RoleVO.Role role) {
        authFacade.addRole(role);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新role")
    @PutMapping(value = "/role/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRole(@RequestBody @Valid RoleVO.Role role) {
        authFacade.updateRole(role);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的role")
    @DeleteMapping(value = "/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleById(@RequestParam int id) {
        return new HttpResult<>(authFacade.deleteRoleById(id));
    }

    // role resourcbe
    @ApiOperation(value = "分页查询角色已绑定的资源列表")
    @GetMapping(value = "/role/resource/bind/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ResourceVO.Resource>> queryRoleBindResourcePage(@Valid ResourceParam.BindResourcePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRoleBindResourcePage(pageQuery));
    }

    @ApiOperation(value = "分页查询角色未绑定的资源列表")
    @GetMapping(value = "/role/resource/unbind/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ResourceVO.Resource>> queryRoleUnbindResourcePage(@Valid ResourceParam.BindResourcePageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryRoleUnbindResourcePage(pageQuery));
    }

    @ApiOperation(value = "角色绑定资源")
    @PostMapping(value = "/role/resource/bind", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> bindRoleResource(@RequestBody @Valid RoleResourceVO.RoleResource roleResource) {
        authFacade.bindRoleResource(roleResource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "角色解除绑定资源")
    @DeleteMapping(value = "/role/resource/unbind", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> unbindRoleResource(@RequestParam int id) {
        authFacade.unbindRoleResource(id);
        return HttpResult.SUCCESS;
    }

    // resource
    @ApiOperation(value = "分页查询resource列表")
    @GetMapping(value = "/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ResourceVO.Resource>> queryResourcePage(@Valid ResourceParam.PageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryResourcePage(pageQuery));
    }

    @ApiOperation(value = "新增resource")
    @PostMapping(value = "/resource/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addResource(@RequestBody @Valid ResourceVO.Resource resource) {
        authFacade.addResource(resource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新resource")
    @PutMapping(value = "/resource/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResource(@RequestBody @Valid ResourceVO.Resource resource) {
        authFacade.updateResource(resource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新resource-needAuth")
    @PutMapping(value = "/resource/updateNeedAuth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResourceNeedAuth(@RequestBody @Valid ResourceVO.Resource resource) {
        authFacade.updateResourceNeedAuth(resource);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的resource")
    @DeleteMapping(value = "/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteResourceById(@RequestParam int id) {
        return new HttpResult<>(authFacade.deleteResourceById(id));
    }

    // resource-group
    @ApiOperation(value = "分页查询resource group列表")
    @GetMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GroupVO.Group>> queryGroupPage(@Valid GroupParam.PageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryGroupPage(pageQuery));
    }

    @ApiOperation(value = "新增resource group")
    @PostMapping(value = "/group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGroup(@RequestBody @Valid GroupVO.Group group) {
        authFacade.addGroup(group);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新resource group")
    @PutMapping(value = "/group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGroup(@RequestBody @Valid GroupVO.Group group) {
        authFacade.updateGroup(group);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的resource group")
    @DeleteMapping(value = "/group/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteGroupById(@RequestParam int id) {
        return new HttpResult<>(authFacade.deleteGroupById(id));
    }

    @ApiOperation(value = "分页查询用户角色列表")
    @GetMapping(value = "/user/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserRoleVO.UserRole>> queryUserRolePage(@Valid UserRoleParam.PageQuery pageQuery) {
        return new HttpResult<>(authFacade.queryUserRolePage(pageQuery));
    }

    @ApiOperation(value = "同步用户角色列(from opscloud)")
    @GetMapping(value = "/user/role/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUserRole() {
        authFacade.syncUserRole();
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "新增用户角色")
    @PostMapping(value = "/user/role/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addUserRole(@RequestBody @Valid UserRoleVO.UserRole userRole) {
        authFacade.addUserRole(userRole);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的用户角色")
    @DeleteMapping(value = "/user/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteUserRoleById(@RequestParam int id) {
        return new HttpResult<>(authFacade.deleteUserRoleById(id));
    }

    @ApiOperation(value = "用户查询菜单")
    @GetMapping(value = "/menu/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<MenuVO>> queryUserMenu() {
        return new HttpResult<>(authFacade.queryUserMenu());
    }


    @ApiOperation(value = "保存角色菜单配置")
    @PutMapping(value = "/role/menu/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveRoleMenu(@RequestBody @Valid AuthMenuVO.Menu menu) {
        return new HttpResult<>(authFacade.saveRoleMenu(menu));
    }

    @ApiOperation(value = "查询角色菜单配置")
    @GetMapping(value = "/role/menu/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AuthMenuVO.Menu> queryRoleMenuByRoleId(@RequestParam int roleId) {
        return new HttpResult<>(authFacade.queryRoleMenuByRoleId(roleId));
    }

    @ApiOperation(value = "吊销所有用户token")
    @PutMapping(value = "/token/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeAllUserToken() {
        tokenFacade.revokeAllUserToken();
        return HttpResult.SUCCESS;
    }


}
