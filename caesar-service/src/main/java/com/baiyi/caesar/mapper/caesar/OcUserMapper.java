package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.user.UserParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcUserMapper extends Mapper<OcUser> {

    OcUser queryByUsername(@Param("username") String username);

    List<OcUser> queryOcUserByParam(UserParam.UserPageQuery pageQuery);

    List<OcUser> fuzzyQueryUserByParam(UserParam.UserPageQuery pageQuery);

    int updateBaseUser(OcUser ocUser);

    List<OcUser> queryOcUserByUserGroupId(@Param("userGroupId")  int userGroupId);

}