package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserGroup;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.ldap.repo.PersonRepo;
import com.baiyi.caesar.service.user.OcUserGroupService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/25 12:06 下午
 * @Version 1.0
 */
@Component("UserDecorator")
public class UserDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private UserApiTokenDecorator userApiTokenDecorator;

    @Resource
    private UserGroupDecorator userGroupDecorator;

    @Resource
    private PersonRepo personRepo;

    @Resource
    private UserCredentialDecorator userCredentialDecorator;

    @Resource
    private UserPermissionServerGroupDecorator userPermissionServerGroupDecorator;

    public void decorator(UserVO.IUser iUser) {
        OcUser ocUser =   ocUserService.queryOcUserByUsername(iUser.getUsername());
        if(ocUser != null)
            iUser.setUser(BeanCopierUtil.copyProperties(ocUser, UserVO.User.class));
    }

    // from mysql
    public UserVO.User decorator(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend == null || extend == 0) return user;
        userGroupDecorator.decorator(user);  // 装饰 用户组
        userPermissionServerGroupDecorator.decorator(user); // 装饰 服务器组
        userApiTokenDecorator.decorator(user);  // 装饰 ApiToken
        userCredentialDecorator.decorator(user);    // 装饰 凭据
        // 用户属性
        Map<String, Object> attributeMap = Maps.newHashMap();
        user.setAttributeMap(attributeMap);

        return user;
    }

    public UserVO.User decoratorFromLdapRepo(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend == null || extend == 0) return user;
        List<UserGroupVO.UserGroup> userGroups = Lists.newArrayList();
        personRepo.searchUserGroupByUsername(user.getUsername()).forEach(e -> {
            OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(e);
            if (ocUserGroup != null)
                userGroups.add(BeanCopierUtil.copyProperties(ocUserGroup, UserGroupVO.UserGroup.class));
        });
        user.setUserGroups(userGroups);

        return user;
    }


}
