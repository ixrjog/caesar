package com.baiyi.caesar.account.impl;


import com.baiyi.caesar.account.IAccount;
import com.baiyi.caesar.account.builder.UserBuilder;
import com.baiyi.caesar.account.convert.LdapPersonConvert;
import com.baiyi.caesar.common.base.AccountType;
import com.baiyi.caesar.common.util.PasswordUtil;
import com.baiyi.caesar.domain.generator.caesar.OcAccount;
import com.baiyi.caesar.domain.generator.caesar.OcAuthRole;
import com.baiyi.caesar.domain.generator.caesar.OcAuthUserRole;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.ldap.entry.Person;
import com.baiyi.caesar.ldap.repo.GroupRepo;
import com.baiyi.caesar.ldap.repo.PersonRepo;
import com.baiyi.caesar.service.auth.OcAuthRoleService;
import com.baiyi.caesar.service.auth.OcAuthUserRoleService;
import com.google.common.collect.Lists;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.BASE_ROLE_NAME;

/**
 * @Author baiyi
 * @Date 2020/1/3 5:14 下午
 * @Version 1.0
 */
@Component("LdapAccount")
public class LdapAccount extends BaseAccount implements IAccount {

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private PersonRepo personRepo;

    @Resource
    private GroupRepo groupRepo;

    @Resource
    private OcAuthUserRoleService ocAuthUserRoleService;

    @Resource
    private OcAuthRoleService ocAuthRoleService;


    @Override
    protected List<OcUser> getUserList() {
        return personRepo.getPersonList().stream().map(UserBuilder::build).collect(Collectors.toList());
    }


    @Override
    protected int getAccountType() {
        return AccountType.LDAP.getType();
    }

    @Override
    protected List<OcAccount> getOcAccountList() {
        return Lists.newArrayList();
    }


    /**
     * 创建
     *
     * @return
     */
    @Override
    public Boolean create(OcUser user) {
        user.setIsActive(true);
        user.setSource("ldap");
        // 若密码为空生成初始密码
        String password = (StringUtils.isEmpty(user.getPassword()) ? PasswordUtil.getPW(PASSWORD_LENGTH) : user.getPassword());
        user.setPassword(stringEncryptor.encrypt(password)); // 加密
        ocUserService.addOcUser(user);
        initialUserBaseRole(user); // 初始化角色
        // 初始化默认角色
        return personRepo.create(LdapPersonConvert.convertOcUser(user, password));
    }

    private void initialUserBaseRole(OcUser user) {
        try {
            OcAuthUserRole ocAuthUserRole = new OcAuthUserRole();
            ocAuthUserRole.setUsername(user.getUsername());
            OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleByName(BASE_ROLE_NAME);
            ocAuthUserRole.setRoleId(ocAuthRole.getId());
            ocAuthUserRoleService.addOcAuthUserRole(ocAuthUserRole);
        } catch (Exception ignored) {
        }
    }

    /**
     * 移除
     *
     * @return
     */
    @Override
    public Boolean delete(OcUser user) {
        try {
            if (personRepo.delete(user.getUsername())) {
                ocUserService.delOcUserByUsername(user.getUsername());
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean active(OcUser user, boolean active) {
        if (!active) {
            if (!personRepo.checkPersonInLdap(user.getUsername()))
                return true; // 用户不存在
            Person person = new Person();
            person.setUsername(user.getUsername());
            person.setUserPassword(PasswordUtil.getPW(20));
            personRepo.update(person);
        }
        return true;
    }

    @Override
    public Boolean update(OcUser user) {
        // 校验用户
        OcUser ocUser;
        if (!StringUtils.isEmpty(user.getUsername())) {
            ocUser = ocUserService.queryOcUserByUsername(user.getUsername());
        } else {
            ocUser = ocUserService.queryOcUserById(user.getId());
        }
        if (ocUser == null) return Boolean.FALSE;
        Person person = new Person();
        person.setUsername(ocUser.getUsername());
        if (!StringUtils.isEmpty(user.getDisplayName())) {
            //ocUser.setDisplayName(user.getDisplayName());
            person.setDisplayName(user.getDisplayName());
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            //ocUser.setEmail(user.getEmail());
            person.setEmail(user.getEmail());
        }
        if (!StringUtils.isEmpty(user.getPhone())) {
            //ocUser.setPhone(user.getPhone());
            person.setMobile(user.getPhone());
        }

        if (!StringUtils.isEmpty(user.getPassword())) {
            //ocUser.setPassword(stringEncryptor.encrypt(user.getPassword())); // 加密
            person.setUserPassword(user.getPassword());
        }
        try {
            //ocUserService.updateOcUser(ocUser);
            personRepo.update(person);
            return Boolean.TRUE;
        } catch (Exception ignored) {
        }
        return Boolean.FALSE;
    }

    /**
     * 授权
     *
     * @param user
     * @param resource
     * @return
     */
    @Override
    public Boolean grant(OcUser user, String resource) {
        return groupRepo.addGroupMember(resource, user.getUsername());
    }

    /**
     * 吊销
     *
     * @param user
     * @param resource
     * @return
     */
    @Override
    public Boolean revoke(OcUser user, String resource) {
        return groupRepo.removeGroupMember(resource, user.getUsername());
    }


}
