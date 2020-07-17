package com.baiyi.opscloud.jenkins.server;

import com.google.common.collect.Maps;
import com.offbytwo.jenkins.JenkinsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/16 2:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsServerContainer implements InitializingBean {

    private static Map<String, JenkinsServer> serverContainer;


    private void initialServer(String url, String username, String passwordOrToken) {
        JenkinsServerContainer.serverContainer = Maps.newConcurrentMap();
        try {
            URI host = new URI(url);
            JenkinsServer jenkins = new JenkinsServer(host, username, passwordOrToken);
            serverContainer.put("aaa", jenkins);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterPropertiesSet() {
    }
}
