package com.baiyi.caesar.user;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.RegexUtil;
import com.baiyi.caesar.common.util.UUIDUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.user.UserParam;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/11 9:30 上午
 * @Version 1.0
 */
public class UserTest extends BaseUnit {
    @Resource
    private OcUserService ocUserService;

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Test
    void updateUsersUuid() {
        UserParam.UserPageQuery pageQuery = new UserParam.UserPageQuery();
        pageQuery.setUsername("");
        pageQuery.setLength(10000);
        pageQuery.setPage(1);
        DataTable<OcUser> table = ocUserService.queryOcUserByParam(pageQuery);
        System.err.println(JSON.toJSONString(table));
        for (OcUser ocUser : table.getData()) {
            ocUser.setUuid(UUIDUtil.getUUID());
            ocUserService.updateOcUser(ocUser);
        }

    }

    @Test
    void accessLevelTest() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("xujian");
        int accessLevel = userPermissionFacade.getUserAccessLevel(ocUser);
        System.err.println(JSON.toJSONString(accessLevel));
    }

    @Test
    void aa() {
        String userStr =


                "shiluo\n" +

                        "zuobing";

        String[] users = userStr.split("\\n");
        for (String user : users) {
            System.err.println(user);
            OcUser ocUser =
                    ocUserService.queryOcUserByUsername(user);
            if (ocUser == null) continue;

            userFacade.retireUser(ocUser.getId());

        }

    }

    @Test
    void b() {
        OcUser ocUser =
                ocUserService.queryOcUserByUsername("ouyanggw");
        boolean r = RegexUtil.isPhone(ocUser.getPhone());
        System.err.println(r);
    }

    @Test
    void beReinstatedUserTest() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("guannan");
        System.err.println(JSON.toJSONString(ocUser));
        userFacade.beReinstatedUser(ocUser.getId());

    }

}
