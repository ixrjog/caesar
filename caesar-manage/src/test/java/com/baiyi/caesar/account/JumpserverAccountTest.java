package com.baiyi.caesar.account;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.account.factory.AccountFactory;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/14 5:13 下午
 * @Version 1.0
 */
public class JumpserverAccountTest extends BaseUnit {

    private static final String key = "JumpserverAccount";

    private IAccount getAccount() {
        return AccountFactory.getAccountByKey(key);
    }

    @Resource
    private OcUserService ocUserService;

    @Test
    void testPushKey() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("shutiao");
        getAccount().pushSSHKey(ocUser);

    }

}
