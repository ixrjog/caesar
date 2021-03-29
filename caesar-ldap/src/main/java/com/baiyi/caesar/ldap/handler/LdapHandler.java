package com.baiyi.caesar.ldap.handler;


import com.baiyi.caesar.ldap.config.LdapConfig;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.entry.Person;
import com.baiyi.caesar.ldap.mapper.GroupAttributesMapper;
import com.baiyi.caesar.ldap.mapper.PersonAttributesMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.naming.directory.*;
import java.util.Collections;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Ldap 通用处理
 */
@Slf4j
@Component
public class LdapHandler {

    @Resource
    private LdapTemplate ldapTemplate;

    @Resource
    private LdapConfig ldapConfig;

    /**
     * 查询所有Person
     *
     * @return
     */
    public List<Person> queryPersonList() {
        return ldapTemplate.search(query().where("objectclass").is(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_OBJECT_CLASS)), new PersonAttributesMapper());
    }

    /**
     * 查询所有Person username
     *
     * @return
     */
    public List<String> queryPersonNameList() {
        return ldapTemplate.search(
                query().where("objectclass").is(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_OBJECT_CLASS)), (AttributesMapper<String>) attrs
                        -> (String) attrs.get(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_ID)).get());
    }

    /**
     * 通过dn查询Person
     *
     * @param dn
     * @return
     */
    public Person getPersonWithDn(String dn) {
        return ldapTemplate.lookup(dn, new PersonAttributesMapper());
    }

    /**
     * 校验账户
     *
     * @param credential
     * @return
     */
    public boolean loginCheck(com.baiyi.caesar.ldap.credential.PersonCredential credential) {
        if (credential.isEmpty()) return false;
        String username = credential.getUsername();
        String password = credential.getPassword();
        log.info("login check content username {}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_ID), username));
        try {
            return ldapTemplate.authenticate(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_BASE_DN), filter.toString(), password);
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    /**
     * 解除绑定
     *
     * @param dn
     */
    public void unbind(String dn) {
        ldapTemplate.unbind(dn);
    }

    private boolean bind(String dn, Object obj, Attributes attrs) {
        try {
            ldapTemplate.bind(dn, obj, attrs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 绑定用户
     *
     * @param person
     * @return
     */
    public boolean bindPerson(Person person) {
        String userId = ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_ID);
        String userBaseDN = ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_BASE_DN);
        String userObjectClass = ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_OBJECT_CLASS);

        try {
            String rdn = Joiner.on("=").join(userId, person.getUsername());
            String dn = Joiner.on(",").skipNulls().join(rdn, userBaseDN);
            // 基类设置
            BasicAttribute ocattr = new BasicAttribute("objectClass");
            ocattr.add("top");
            ocattr.add("person");
            ocattr.add("organizationalPerson");
            if (!userObjectClass.equalsIgnoreCase("person") && !userObjectClass.equalsIgnoreCase("organizationalPerson"))
                ocattr.add(userObjectClass);
            // 用户属性
            Attributes attrs = new BasicAttributes();
            attrs.put(ocattr);
            attrs.put(userId, person.getUsername()); // cn={username}
            attrs.put("sn", person.getUsername());
            attrs.put("displayName", person.getDisplayName());
            attrs.put("mail", person.getEmail());
            attrs.put("userPassword", person.getUserPassword());
            attrs.put("mobile", (StringUtils.isEmpty(person.getMobile()) ? "0" : person.getMobile()));
            if (bind(dn, null, attrs))
                return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updatePerson(Person person) {
        String rdn = Joiner.on("=").join(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_ID), person.getUsername());
        String dn = Joiner.on(",").skipNulls().join(rdn, ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_BASE_DN));
        Person checkPerson = getPersonWithDn(dn);
        if (checkPerson == null) return false;
        try {
            if (!StringUtils.isEmpty(person.getDisplayName()) && !person.getDisplayName().equals(checkPerson.getDisplayName()))
                modifyAttributes(dn, "displayName", person.getDisplayName());
            if (!StringUtils.isEmpty(person.getEmail()) && !person.getEmail().equals(checkPerson.getEmail()))
                modifyAttributes(dn, "mail", person.getEmail());
            if (!StringUtils.isEmpty(person.getMobile()) && !person.getMobile().equals(checkPerson.getMobile()))
                modifyAttributes(dn, "mobile", person.getMobile());
            if (!StringUtils.isEmpty(person.getUserPassword()))
                modifyAttributes(dn, "userpassword", person.getUserPassword());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有Group
     *
     * @return
     */
    public List<Group> queryGroupList() {
        return ldapTemplate.search(query().where("objectclass").is(ldapConfig.getCustomByKey(LdapConfig.Attributes.GROUP_OBJECT_CLASS)), new GroupAttributesMapper());
    }

    public List<String> queryGroupMember(String groupName) {
        try {
            //String dn = Joiner.on("").join("cn=", groupName, ",", ldapConfig.getCustomByKey(LdapConfig.GROUP_BASE_DN));
            DirContextAdapter adapter = (DirContextAdapter) ldapTemplate.lookup(ldapConfig.buildGroupDN(groupName));
            //"uniqueMember"
            // LdapConfig.GROUP_MEMBER
            String[] members = adapter.getStringAttributes(ldapConfig.getCustomByKey(LdapConfig.Attributes.GROUP_MEMBER));
            List<String> usernameList = Lists.newArrayList();
            for (String member : members) {
                String[] m = member.split("=|,");
                if (m.length > 2 && !m[1].equals("admin"))
                    usernameList.add(m[1]);
            }
            return usernameList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean removeGroupMember(String groupName, String username) {
        return modificationGroupMember(groupName,username,DirContext.REMOVE_ATTRIBUTE);
    }

    public boolean addGroupMember(String groupName, String username) {
        return modificationGroupMember(groupName,username,DirContext.ADD_ATTRIBUTE);
    }

    private boolean modificationGroupMember(String groupName, String username, int modificationType) {
        String groupDN = ldapConfig.buildGroupDN(groupName);
        String groupMember = ldapConfig.getCustomByKey(LdapConfig.Attributes.GROUP_MEMBER);
        String userDN = ldapConfig.buildUserFullDN(username);
        try {
            ldapTemplate.modifyAttributes(groupDN, new ModificationItem[]{
                    new ModificationItem(modificationType, new BasicAttribute(groupMember, userDN))
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    private void modifyAttributes(String dn, String attrId, String value) {
        ldapTemplate.modifyAttributes(dn, new ModificationItem[]{
                new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attrId, value))
        });
    }

    public boolean checkPersonInLdap(String username) {
        try {
            DirContextAdapter adapter = (DirContextAdapter) ldapTemplate.lookup(ldapConfig.buildUserDN(username));
            String cn = adapter.getStringAttribute(ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_ID));
            if (username.equalsIgnoreCase(cn)) return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public List<String> searchLdapGroup(String username) {
        List<String> groupList = Lists.newArrayList();
        try {
            String groupBaseDN = ldapConfig.getCustomByKey(LdapConfig.Attributes.GROUP_BASE_DN);
            String groupMember = ldapConfig.getCustomByKey(LdapConfig.Attributes.GROUP_MEMBER);
            String userId = ldapConfig.getCustomByKey(LdapConfig.Attributes.USER_ID);
            String userDN = ldapConfig.buildUserFullDN(username);
            groupList = ldapTemplate.search(LdapQueryBuilder.query().base(groupBaseDN)
                            .where(groupMember).is(userDN).and(userId).like("*"),
                    (AttributesMapper<String>) attributes -> attributes.get(userId).get(0).toString()
            );
        } catch (Exception e) {
            log.warn("username={} search ldap group error={}", username, e.getMessage(), e);
        }
        return groupList;
    }

}
