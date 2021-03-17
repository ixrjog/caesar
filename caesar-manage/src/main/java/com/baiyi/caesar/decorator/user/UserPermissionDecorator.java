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
    private OcUserPermissionService ocUserPermissionService;

    // from mysql
    public void decorator(UserVO.IUserPermission iUserPermission){
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(iUserPermission.getUserId())
                .businessType(iUserPermission.getBusinessType())
                .businessId(iUserPermission.getBusinessId())
                .build();
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
        if (ocUserPermission != null)
            iUserPermission.setUserPermission(BeanCopierUtil.copyProperties(ocUserPermission, UserPermissionVO.UserPermission.class));
    }

}
