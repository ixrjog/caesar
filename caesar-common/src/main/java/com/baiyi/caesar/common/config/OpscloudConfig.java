package com.baiyi.caesar.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/9/8 5:34 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "opscloud", ignoreInvalidFields = true)
public class OpscloudConfig  {

    private String url;
    private String apiTokenId;
    private String apiToken;

}