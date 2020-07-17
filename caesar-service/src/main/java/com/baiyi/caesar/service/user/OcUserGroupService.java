package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcUserGroup;
import com.baiyi.caesar.domain.param.user.UserBusinessGroupParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:59 下午
 * @Version 1.0
 */
public interface OcUserGroupService {

    DataTable<OcUserGroup> queryOcUserGroupByParam(UserBusinessGroupParam.PageQuery pageQuery);

    DataTable<OcUserGroup> queryUserIncludeOcUserGroupByParam(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    DataTable<OcUserGroup> queryUserExcludeOcUserGroupByParam(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    void addOcUserGroup(OcUserGroup ocUserGroup);

    void updateOcUserGroup(OcUserGroup ocUserGroup);

    OcUserGroup queryOcUserGroupByName(String name);

    OcUserGroup queryOcUserGroupById(int id);

    List<OcUserGroup> queryOcUserGroupByUserId(int userId);

    List<OcUserGroup> queryUserTicketOcUserGroupByParam(UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam);
}
