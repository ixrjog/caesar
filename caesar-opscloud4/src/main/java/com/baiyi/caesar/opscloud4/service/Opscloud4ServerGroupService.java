package com.baiyi.caesar.opscloud4.service;

import com.baiyi.caesar.common.config.Opscloud4Config;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.opscloud4.feign.Opscloud4ServerGroupFeign;
import com.baiyi.caesar.opscloud4.model.ServerGroupModel;
import com.baiyi.caesar.opscloud4.model.ServerModel;
import com.baiyi.caesar.opscloud4.param.ServerGroupParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/25 5:05 下午
 * @Version 1.0
 */
@Service
public class Opscloud4ServerGroupService {

    @Resource
    private Opscloud4Config opscloud4Config;

    private Opscloud4ServerGroupFeign buildFeign() {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(Opscloud4ServerGroupFeign.class, opscloud4Config.getUrl());
    }

    public Map<String, List<ServerModel.Server>> queryServerGroupHostPattern(ServerGroupParam.ServerGroupEnvHostPatternQuery query) {
        Opscloud4ServerGroupFeign oc4API = buildFeign();
        ServerModel.HttpResult httpResult = oc4API.queryServerGroupHostPattern(opscloud4Config.getAccessToken(), query);
        return httpResult.getBody();
    }

    public ServerGroupModel.DataTable queryServerGroupPage(ApplicationParam.ServerGroupPageQuery query){
        Opscloud4ServerGroupFeign oc4API = buildFeign();
        ServerGroupModel.HttpResult httpResult = oc4API.queryServerGroupPage(opscloud4Config.getAccessToken(), query);
        return httpResult.getBody();
    }

}
