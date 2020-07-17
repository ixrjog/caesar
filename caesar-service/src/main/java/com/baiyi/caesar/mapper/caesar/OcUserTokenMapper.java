package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcUserToken;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OcUserTokenMapper extends Mapper<OcUserToken> {

    /**
     * 判断用户是否可访问某个资源
     * @param token
     * @param resourceName
     * @return
     */
    int checkUserHasResourceAuthorize(@Param("token") String token, @Param("resourceName") String resourceName);

    int checkUserHasRole(@Param("token") String token, @Param("roleName") String roleName);

}