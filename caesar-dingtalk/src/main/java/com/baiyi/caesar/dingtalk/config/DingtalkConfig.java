package com.baiyi.caesar.dingtalk.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/7/28 10:36 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "dingtalk", ignoreInvalidFields = true)
public class DingtalkConfig {

    private final static String ACCESS_TOKEN = "access_token";

    private String url;

    public String getWebHook(String dingtalkToken) {
        return Joiner.on("").join(url, "?", ACCESS_TOKEN, "=", dingtalkToken);
    }

}
