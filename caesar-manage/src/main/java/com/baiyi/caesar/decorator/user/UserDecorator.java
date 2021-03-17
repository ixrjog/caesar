package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.base.CredentialType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.user.UserApiTokenVO;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.ldap.repo.PersonRepo;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.baiyi.caesar.service.user.OcUserApiTokenService;
import com.baiyi.caesar.service.user.OcUserCredentialService;
import com.baiyi.caesar.service.user.OcUserGroupService;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/25 12:06 下午
 * @Version 1.0
 */
@Component("UserDecorator")
public class UserDecorator {

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserApiTokenService ocUserApiTokenService;

    @Resource
    private OcUserCredentialService ocUserCredentialService;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    @Resource
    private UserGroupDecorator userGroupDecorator;

    @Resource
    private PersonRepo personRepo;

    // from mysql
    public UserVO.User decorator(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend == null || extend == 0) return user;
        // 装饰 用户组
        userGroupDecorator.decorator(user);

        // 装饰 服务器组
        List<OcServerGroup> serverGroupList = ocServerGroupService.queryUserPermissionOcServerGroupByUserId(user.getId());
        user.setServerGroups(convert(user, serverGroupList));
        // 装饰 ApiToken
        List<OcUserApiToken> userApiTokens = ocUserApiTokenService.queryOcUserApiTokenByUsername(user.getUsername());
        List<UserApiTokenVO.UserApiToken> apiTokens = BeanCopierUtil.copyListProperties(userApiTokens, UserApiTokenVO.UserApiToken.class)
                .stream().peek(e -> e.setToken("申请后不可查看")).collect(Collectors.toList());
        user.setApiTokens(apiTokens);
        // 装饰 凭据
        List<OcUserCredential> credentials = ocUserCredentialService.queryOcUserCredentialByUserId(user.getId());
        Map<String, UserCredentialVO.UserCredential> credentialMap = Maps.newHashMap();
        for (OcUserCredential credential : credentials)
            credentialMap.put(CredentialType.getName(credential.getCredentialType()), BeanCopierUtil.copyProperties(credential, UserCredentialVO.UserCredential.class));
        user.setCredentialMap(credentialMap);
        // 用户属性
        Map<String, Object> attributeMap = Maps.newHashMap();
        user.setAttributeMap(attributeMap);

        return user;
    }

    public UserVO.User decoratorFromLdapRepo(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend != null && extend == 1) {
            List<UserGroupVO.UserGroup> userGroups = Lists.newArrayList();
            personRepo.searchUserGroupByUsername(user.getUsername()).forEach(e -> {
                OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(e);
                if (ocUserGroup != null)
                    userGroups.add(BeanCopierUtil.copyProperties(ocUserGroup, UserGroupVO.UserGroup.class));
            });
            user.setUserGroups(userGroups);
        }
        return user;
    }

    private List<ServerGroupVO.ServerGroup> convert(UserVO.User user, List<OcServerGroup> serverGroupList) {
        return serverGroupList.stream().map(e -> {
            ServerGroupVO.ServerGroup serverGroup = BeanCopierUtil.copyProperties(e, ServerGroupVO.ServerGroup.class);
            OcUserPermission permission = new OcUserPermission();
            permission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());
            permission.setBusinessId(e.getId());
            permission.setUserId(user.getId());
            permission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(permission);
            serverGroup.setIsAdmin(permission != null);
            return serverGroup;
        }).collect(Collectors.toList());
    }
}
