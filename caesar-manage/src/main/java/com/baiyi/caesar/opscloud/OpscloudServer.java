package com.baiyi.caesar.opscloud;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/9/8 5:47 下午
 * @Version 1.0
 */
@Component
public class OpscloudServer {





    public JsonNode httpPostExecutor(Object param) throws IOException {

        String url = "/server/group/pattern/query";

        return OpscloudHttpUtils.httpPostExecutor(url , param);
    }


}
