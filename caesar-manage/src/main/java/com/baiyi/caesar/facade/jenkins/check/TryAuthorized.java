package com.baiyi.caesar.facade.jenkins.check;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.exception.build.BuildRuntimeException;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/4/15 5:23 下午
 * @Version 1.0
 */
@Component
public class TryAuthorized {

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    public void tryAuthorizedUser(CsCiJob csCiJob) throws BuildRuntimeException {
        OcUser operationUser = userFacade.getOcUserBySession();
        // 允许运维操作
        BusinessWrapper<Boolean> checkAccessLevelWrapper = userPermissionFacade.checkAccessLevel(operationUser, AccessLevel.OPS.getLevel());
        if (checkAccessLevelWrapper.isSuccess())
            return;
        // 应用管理员
        OcUserPermission ocUserPermission
                = userPermissionFacade.queryUserPermissionByUniqueKey(operationUser.getId(), BusinessType.APPLICATION.getType(), csCiJob.getApplicationId());
        if (ocUserPermission != null && "ADMIN".equalsIgnoreCase(ocUserPermission.getRoleName()))
            return;

        List<OcUserPermission> permissions = userPermissionFacade.queryBusinessPermission(BusinessType.APPLICATION_BUILD_JOB.getType(), csCiJob.getId());
        // 判断是否有任务授权
        if (CollectionUtils.isEmpty(permissions)) {
            // 鉴权到应用
            if (!userPermissionFacade.tryUserBusinessPermission(operationUser.getId(), BusinessType.APPLICATION.getType(), csCiJob.getApplicationId()))
                throw new BuildRuntimeException(ErrorEnum.AUTHENTICATION_FAILUER);
        } else {
            // 鉴权到job
            if (permissions.stream().noneMatch(e -> e.getUserId().equals(operationUser.getId())))
                throw new BuildRuntimeException(ErrorEnum.APPLICATION_JOB_AUTHENTICATION_FAILUER_2);
        }
    }
}
