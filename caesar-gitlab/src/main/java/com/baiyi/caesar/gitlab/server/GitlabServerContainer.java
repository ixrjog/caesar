package com.baiyi.caesar.gitlab.server;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.GitlabAPI;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/20 9:58 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitlabServerContainer implements InitializingBean {

    private static Map<String, GitlabAPI> gitlabAPIContainer;

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private StringEncryptor stringEncryptor;

//    private static String createRandomString() {
//        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
//    }

    private GitlabAPI buildGitlabAPI(CsGitlabInstance csGitlabInstance) {
        return GitlabAPI.connect(csGitlabInstance.getUrl(), stringEncryptor.decrypt(csGitlabInstance.getToken()));
    }

    private void initialGitlabAPI() {
        GitlabServerContainer.gitlabAPIContainer = Maps.newHashMap();
        csGitlabInstanceService.queryAll().forEach(e -> {
            try {
                GitlabAPI gitlabAPI = buildGitlabAPI(e);
                GitlabServerContainer.gitlabAPIContainer.put(e.getName(), gitlabAPI);
            } catch (Exception ignored) {
            }
        });
    }

    public static GitlabAPI getGitlabAPI(String name) {
        if (GitlabServerContainer.gitlabAPIContainer.containsKey(name))
            return GitlabServerContainer.gitlabAPIContainer.get(name);
        return null;
    }

    /**
     * 重置server,用于配置修改后
     */
    public void reset() {
        if (gitlabAPIContainer != null && !gitlabAPIContainer.isEmpty())
            gitlabAPIContainer.keySet().forEach(k -> {
                GitlabAPI gitlabAPI = gitlabAPIContainer.get(k);
                gitlabAPI = null;
            });
        gitlabAPIContainer = null;
        initialGitlabAPI();
    }

    @Override
    public void afterPropertiesSet() {
        initialGitlabAPI();
    }
}
