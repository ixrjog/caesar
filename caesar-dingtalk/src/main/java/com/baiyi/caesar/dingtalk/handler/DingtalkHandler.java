package com.baiyi.caesar.dingtalk.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.dingtalk.content.DingtalkContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/7/28 10:38 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class DingtalkHandler {

    private Map<String, List<String>> notifyMap = new ConcurrentHashMap<>();

    /**
     * 基于合适的上下文，发起钉钉通知
     *
     * @param content
     * @return
     */
    public boolean doNotify(DingtalkContent content) throws IOException {
        if (StringUtils.isEmpty(content.getWebHook()))
            return false;
        List<String> notifyList = notifyMap.computeIfAbsent(content.getWebHook(), k -> new ArrayList<>());
        notifyList.add(content.getMsg());
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(content.getWebHook());
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(content.getMsg(), "utf-8");
        httppost.setEntity(se);
        HttpResponse response = httpclient.execute(httppost);
        String result = EntityUtils.toString(response.getEntity());
        log.warn("notify dingtalk content={} result={}", JSON.toJSONString(content), result);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return true;
        } else {
            return false;
        }
    }
}
