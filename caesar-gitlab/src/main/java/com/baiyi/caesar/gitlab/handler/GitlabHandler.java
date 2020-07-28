package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import org.gitlab.api.models.GitlabVersion;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/7/20 10:23 上午
 * @Version 1.0
 */
@Component
public class GitlabHandler {

    public GitlabVersion getVersion(String gitlabName) throws IOException {
        return GitlabServerContainer.getGitlabAPI(gitlabName).getVersion();
    }
}
