package com.baiyi.caesar.jenkins.server;

import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.JenkinsServer;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private StringEncryptor stringEncryptor;

    private JenkinsServer buildServer(CsJenkinsInstance jenkinsInstance) {
        try {
            URI host = new URI(jenkinsInstance.getUrl());
            return new JenkinsServer(host, jenkinsInstance.getUsername(), stringEncryptor.decrypt(jenkinsInstance.getToken()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initialServer() {
        JenkinsServerContainer.serverContainer = Maps.newHashMap();
        csJenkinsInstanceService.queryAll().forEach(e -> {
            try {
                JenkinsServer jenkinsServer = buildServer(e);
                if (jenkinsServer != null)
                    JenkinsServerContainer.serverContainer.put(e.getName(), jenkinsServer);
            } catch (Exception ignored) {
            }
        });
    }

    public static JenkinsServer getJenkinsServer(String name) {
        if (JenkinsServerContainer.serverContainer.containsKey(name))
            return JenkinsServerContainer.serverContainer.get(name);
        return null;
    }

    /**
     * 重置server,用于配置修改后
     */
    public void reset() {
        if (serverContainer != null && !serverContainer.isEmpty())
            serverContainer.keySet().forEach(k -> {
                JenkinsServer jenkinsServer = serverContainer.get(k);
                jenkinsServer = null;
            });
        serverContainer = null;
        initialServer();
    }

    @Override
    public void afterPropertiesSet() {
        initialServer();
    }
}
