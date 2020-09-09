package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.domain.vo.user.UserVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/25 7:11 下午
 * @Version 1.0
 */
public interface UserPermissionFacade {

    void syncUserBusinessPermission(List<UserVO.User> userList, int businessType, int businessId);

    void syncUserBusinessPermission(int userId, int businessType, List<Integer> businessIds);

    BusinessWrapper<Boolean> addOcUserPermission(OcUserPermission ocUserPermission);

    BusinessWrapper<Boolean> delOcUserPermission(OcUserPermission ocUserPermission);

    void delOcUserPermissionById(int id);

    boolean tryUserBusinessPermission(int userId, int businessType, int businessId);

    OcUserPermission queryUserPermissionByUniqueKey(int userId, int businessType, int businessId);

    /**
     * 查询用户的访问级别
     *
     * @param ocUser
     * @return
     */
    int getUserAccessLevel(OcUser ocUser);

    BusinessWrapper<Boolean> checkAccessLevel(OcUser ocUser, int accessLevel);
}
