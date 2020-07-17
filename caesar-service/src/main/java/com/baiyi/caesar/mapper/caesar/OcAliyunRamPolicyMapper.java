package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcAliyunRamPolicy;
import com.baiyi.caesar.domain.param.cloud.AliyunRAMPolicyParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunRamPolicyMapper extends Mapper<OcAliyunRamPolicy> {

    List<OcAliyunRamPolicy> queryOcAliyunRamPolicyByUserPermission(@Param("accountUid") String accountUid, @Param("userId") int userId);

    List<OcAliyunRamPolicy> queryOcAliyunRamPolicyByParam(AliyunRAMPolicyParam.PageQuery pageQuery);

    List<OcAliyunRamPolicy>queryUserTicketOcRamPolicyByParam(AliyunRAMPolicyParam.UserTicketOcRamPolicyQuery queryParam);

}