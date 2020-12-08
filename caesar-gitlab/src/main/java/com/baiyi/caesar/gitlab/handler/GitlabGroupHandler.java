package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabNamespace;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2020/7/20 6:24 下午
 * @Version 1.0
 */
@Component
public class GitlabGroupHandler {

    public List<GitlabGroup> getGroups(String gitlabName) throws IOException {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getGroups();
    }

    public List<GitlabNamespace> getNamespace(String gitlabName) {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getNamespaces();
    }
}
