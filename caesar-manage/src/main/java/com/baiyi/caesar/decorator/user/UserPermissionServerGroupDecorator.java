package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/17 5:15 下午
 * @Version 1.0
 */
@Component
public class UserPermissionServerGroupDecorator {

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    public void decorator(UserVO.User user) {
        // 装饰 服务器组
        List<OcServerGroup> serverGroupList = ocServerGroupService.queryUserPermissionOcServerGroupByUserId(user.getId());
        user.setServerGroups(convert(user, serverGroupList));
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
