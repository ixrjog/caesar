package com.baiyi.caesar.ldap;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.ldap.credential.PersonCredential;
import com.baiyi.caesar.ldap.handler.LdapHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/7 5:03 下午
 * @Version 1.0
 */
public class LdapTest extends BaseUnit {

    @Resource
    private LdapHandler ldapHandler;

    @Test
    void testLoginCheck() {
        for(int i=1;i<= 20;i++ ){
            com.baiyi.caesar.ldap.credential.PersonCredential credential = PersonCredential.builder()
                    .username("zzz-test2")
                    .password("1111111111111")
                    .build();
            System.err.println(ldapHandler.loginCheck(credential));
        }


    }

}
