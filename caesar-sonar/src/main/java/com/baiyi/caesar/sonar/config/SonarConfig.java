package com.baiyi.caesar.sonar.config;

import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

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

    public boolean isEmpty() {
        return (StringUtils.isEmpty(token));
    }

    public String toBasic() {
        String authString = token + ":";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8));
        return new String(authEncBytes);
    }

}
