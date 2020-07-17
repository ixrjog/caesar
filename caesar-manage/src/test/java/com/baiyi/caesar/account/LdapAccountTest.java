package com.baiyi.caesar.account;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.account.factory.AccountFactory;
import com.baiyi.caesar.common.util.PasswordUtils;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/8 6:46 下午
 * @Version 1.0
 */
public class LdapAccountTest extends BaseUnit {

    private static final String key = "LdapAccount";

    private IAccount getAccount(){
        return  AccountFactory.getAccountByKey(key);
    }

    @Test
    void testRsync() {
        getAccount().sync();
    }

    @Test
    void testCreateUser() {
        getAccount().create(getOcUser());
    }

    @Test
    void testDeleteUser() {
        getAccount().delete(getOcUser());
    }

    /**
     * 更新用户属性
     */
    @Test
    void testUpdateUser() {
        OcUser ocUser = getOcUser();
        String password = PasswordUtils.getPW(16);
        ocUser.setPassword(password);
        System.err.println(password);
        getAccount().update(ocUser);
    }

    private OcUser getOcUser() {
        OcUser user = new OcUser();
        user.setUsername("oc3-test");
        user.setDisplayName("oc3测试用户2");
        user.setEmail("oc3-test2@gegejia.com");
        user.setPhone("13456768043");
        return user;
    }

}
