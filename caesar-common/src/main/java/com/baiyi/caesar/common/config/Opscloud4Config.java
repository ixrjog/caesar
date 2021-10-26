package com.baiyi.caesar.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/10/25 5:08 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "opscloud4", ignoreInvalidFields = true)
public class Opscloud4Config {

    private String url;
    private String tokenId;
    private String accessToken;

}
