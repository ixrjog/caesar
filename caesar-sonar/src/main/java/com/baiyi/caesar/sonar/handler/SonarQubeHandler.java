package com.baiyi.caesar.sonar.handler;

import com.baiyi.caesar.common.util.JSONMapper;
import com.baiyi.caesar.sonar.config.SonarConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @Author baiyi
 * @Date 2020/12/24 10:04 上午
 * @Version 1.0
 */
@Component
public class SonarQubeHandler {

    private static SonarConfig sonarConfig;

    private static final String X_API_KEY = "X-Api-Key";

    @Autowired
    private void setSonarConfig(SonarConfig sonarConfig) {
        SonarQubeHandler.sonarConfig = sonarConfig;
    }

    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 10 * 1000;

    private static JsonNode readTree(byte[] data) throws JsonProcessingException {
        JSONMapper mapper = new JSONMapper();
        return mapper.readTree(new String(data));
    }

    private static RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
    }

    private static String buildUrl(String url) {
        return Joiner.on("/").join(sonarConfig.getUrl(), url);
    }

    public static JsonNode httpGetExecutor(String api, Map<String, String> param) throws IOException {
        HttpGet httpGet = new HttpGet(buildUrl(api) + buildGetParam(param));
        invokeRequest(httpGet);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet, new HttpClientContext());
        HttpEntity entity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        return readTree(data);
    }

    private static void invokeRequest(HttpRequestBase httpRequestBase) {
        httpRequestBase.setConfig(buildRequestConfig());
        httpRequestBase.setHeader("Content-Type", "application/json;charset=utf-8");
        httpRequestBase.setHeader(X_API_KEY, sonarConfig.getToken());
    }

    private static String buildGetParam(Map<String, String> param) {
        if (CollectionUtils.isEmpty(param))
            return "";
        List<String> s = Lists.newArrayListWithCapacity(param.size());
        param.forEach((k, v) -> s.add(Joiner.on("=").join(k, v)));
        return "?" + Joiner.on("&").join(s);
    }

}
