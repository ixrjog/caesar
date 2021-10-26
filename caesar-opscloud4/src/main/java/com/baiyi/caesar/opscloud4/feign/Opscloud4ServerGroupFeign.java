package com.baiyi.caesar.opscloud4.feign;

import com.baiyi.caesar.opscloud4.model.ServerModel;
import com.baiyi.caesar.opscloud4.param.ServerGroupParam;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/10/25 3:30 下午
 * @Version 1.0
 */
public interface Opscloud4ServerGroupFeign {

    @RequestLine("POST /api/server/group/env/pattern/query")
    @Headers({"Content-Type: application/json;charset=utf-8", "AccessToken: {accessToken}"})
    ServerModel.HttpResult queryServerGroupHostPattern(@Param("accessToken") String accessToken, ServerGroupParam.ServerGroupEnvHostPatternQuery query);

}
