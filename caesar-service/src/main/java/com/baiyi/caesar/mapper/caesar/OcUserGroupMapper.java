package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcUserGroup;
import com.baiyi.caesar.domain.param.user.UserBusinessGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcUserGroupMapper extends Mapper<OcUserGroup> {

    List<OcUserGroup> queryOcUserGroupByParam(UserBusinessGroupParam.PageQuery pageQuery);

    List<OcUserGroup> queryOcUserGroupByUserId(int userId);

    // 用户授权用户组
    List<OcUserGroup> queryUserOcUserGroupByParam(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    // 用户未授权用户组
    List<OcUserGroup> queryUserExcludeOcUserGroupByParam(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    List<OcUserGroup> queryUserTicketOcUserGroupByParam(UserBusinessGroupParam.UserTicketOcUserGroupQuery queryParam);

}