package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.user.UserParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/3 6:51 下午
 * @Version 1.0
 */
public interface OcUserService {

    void addOcUser(OcUser ocUser);

    void updateOcUser(OcUser ocUser);

    void updateBaseOcUser(OcUser ocUser);

    OcUser queryOcUserById(Integer id);

    OcUser queryOcUserByUsername(String username);

    void delOcUserByUsername(String username);

    DataTable<OcUser> queryOcUserByParam(UserParam.UserPageQuery pageQuery);

    DataTable<OcUser> fuzzyQueryUserByParam(UserParam.UserPageQuery pageQuery);

    List<OcUser> queryOcUserByUserGroupId(int userGroupId);

    /**
     * 查询所有激活的账户
     * @return
     */
    List<OcUser> queryOcUserActive();

}
