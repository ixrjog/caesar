package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.bo.UserPermissionBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/14 2:05 下午
 * @Version 1.0
 */
@Component
public class UserPermissionDecorator {

    @Resource
    private UserDecorator userDecorator;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    // from mysql
    public UserVO.User decorator(UserVO.User user, int businessType, int businessId) {
        user = userDecorator.decorator(user, 0);

        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(user.getId())
                .businessType(businessType)
                .businessId(businessId)
                .build();
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
        if (ocUserPermission != null) {
            user.setUserPermission(BeanCopierUtil.copyProperties(ocUserPermission, UserPermissionVO.UserPermission.class));
        }
        return user;

    }
}
