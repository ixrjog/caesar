package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.base.BaseDecorator;
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
public class UserDecorator extends BaseDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private PersonRepo personRepo;

    public void decorator(UserVO.IUser iUser) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(iUser.getUsername());
        if (ocUser == null) return;
        UserVO.User user = BeanCopierUtil.copyProperties(ocUser, UserVO.User.class);
        user.setPassword("");
        iUser.setUser(user);
    }

    // from mysql
    public UserVO.User decorator(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend == null || extend == 0) return user;
        decoratorUser(user);
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
