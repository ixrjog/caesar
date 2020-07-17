package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.user.UserServerTreeParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerGroupMapper extends Mapper<OcServerGroup> {

    List<OcServerGroup> queryOcServerGroupByParam(ServerGroupParam.PageQuery pageQuery);

    List<OcServerGroup> queryUserOcServerGroupByParam(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    List<OcServerGroup> queryUserExcludeOcServerGroupByParam(ServerGroupParam.UserServerGroupPageQuery pageQuery);

    List<OcServerGroup> queryUserPermissionOcServerGroupByUserId(@Param("userId") int userId);

    List<OcServerGroup> queryUserPermissionOcServerGroupByParam(UserServerTreeParam.UserServerTreeQuery userServerTreeQuery);

    List<OcServerGroup> queryUserTicketOcServerGroupByParam(ServerGroupParam.UserTicketOcServerGroupQuery userTicketOcServerGroupQuery);

    List<OcServerGroup> queryLogMemberOcServerGroupByParam(ServerGroupParam.LogMemberServerGroupQuery pageQuery);
}
