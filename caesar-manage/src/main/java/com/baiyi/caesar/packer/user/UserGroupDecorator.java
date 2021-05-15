package com.baiyi.caesar.packer.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserGroup;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.ldap.repo.GroupRepo;
import com.baiyi.caesar.service.user.OcUserGroupService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 8:04 下午
 * @Version 1.0
 */
@Component("UserGroupDecorator")
public class UserGroupDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private GroupRepo groupRepo;

    public void decorator(UserGroupVO.IUserGroups iUserGroups) {
        List<OcUserGroup> userGroupList = ocUserGroupService.queryOcUserGroupByUserId(iUserGroups.getUserId());
        iUserGroups.setUserGroups(BeanCopierUtil.copyListProperties(userGroupList, UserGroupVO.UserGroup.class));
    }

    // from mysql
    public UserGroupVO.UserGroup decorator(UserGroupVO.UserGroup userGroup, Integer extend) {
        if (extend == null || extend == 0) return userGroup;
        List<OcUser> userList = ocUserService.queryOcUserByUserGroupId(userGroup.getId());
        userGroup.setUsers(BeanCopierUtil.copyListProperties(userList, UserVO.User.class));
        return userGroup;
    }

    public UserGroupVO.UserGroup decoratorFromLdapRepo(UserGroupVO.UserGroup userGroup, Integer extend) {
        if (extend == null || extend == 0) return userGroup;
        List<String> usernameList = groupRepo.queryGroupMember(userGroup.getName());
        //Map<String, OcUserVO.User> userMap = Maps.newHashMap();
        List<UserVO.User> users = Lists.newArrayList();
        for (String username : usernameList) {
            OcUser ocUser = ocUserService.queryOcUserByUsername(username);
            if (ocUser != null)
                users.add(BeanCopierUtil.copyProperties(ocUser, UserVO.User.class));
        }
        userGroup.setUsers(users);

        return userGroup;
    }
}
