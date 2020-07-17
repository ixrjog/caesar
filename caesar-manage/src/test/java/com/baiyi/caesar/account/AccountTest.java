package com.baiyi.caesar.account;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.vo.auth.UserRoleVO;
import com.baiyi.caesar.facade.AuthFacade;
import com.baiyi.caesar.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/6 4:15 下午
 * @Version 1.0
 */
public class AccountTest extends BaseUnit {


    @Resource
    private OcUserService ocUserService;


    @Resource
    private AuthFacade authFacade;

    @Test
    void testAddBaseRole() {
        List<OcUser> userList = ocUserService.queryOcUserActive();
        for (OcUser ocUser : userList) {
            UserRoleVO.UserRole userRole = new UserRoleVO.UserRole();
            userRole.setUsername(ocUser.getUsername());
            userRole.setRoleId(5);

            authFacade.addUserRole(userRole);
            System.err.println("username="+ocUser.getUsername());
        }


    }


}
