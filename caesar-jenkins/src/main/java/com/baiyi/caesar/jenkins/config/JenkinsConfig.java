package com.baiyi.caesar.jenkins.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/7/21 1:56 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "jenkins", ignoreInvalidFields = true)
public class JenkinsConfig {

    private Chanelog chanelog;
    private Template template;

    @Data
    public static class Chanelog {
        private String rss;
        private String rssZh;
    }

    @Data
    public static class Template {
        private String prefix;
    }

}
