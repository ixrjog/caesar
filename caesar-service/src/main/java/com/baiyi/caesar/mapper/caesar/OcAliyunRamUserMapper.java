package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAliyunRamUser;
import com.baiyi.caesar.domain.param.cloud.AliyunRAMUserParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunRamUserMapper extends Mapper<OcAliyunRamUser> {

    List<OcAliyunRamUser> queryOcAliyunRamUserByParam(AliyunRAMUserParam.PageQuery pageQuery);

    List<OcAliyunRamUser> queryUserPermissionRamUserByUserId(int userId);
}