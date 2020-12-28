package com.baiyi.caesar.sonar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/12/24 9:57 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "sonar", ignoreInvalidFields = true)
public class SonarConfig {

    private String url;
    private String token;

}
