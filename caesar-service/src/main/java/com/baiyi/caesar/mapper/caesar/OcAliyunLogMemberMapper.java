package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAliyunLogMember;
import com.baiyi.caesar.domain.param.cloud.AliyunLogMemberParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunLogMemberMapper extends Mapper<OcAliyunLogMember> {

    List<OcAliyunLogMember> queryOcAliyunLogMemberByParam(AliyunLogMemberParam.PageQuery pageQuery);
}