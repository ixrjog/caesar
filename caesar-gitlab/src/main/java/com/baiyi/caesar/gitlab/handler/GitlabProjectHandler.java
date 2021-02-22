package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2020/6/20 1:53 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectHandler {

    public List<GitlabProject> getProjects(String gitlabName) {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getAllProjects();
    }

    public GitlabProject getProject(String gitlabName, Serializable projectId) throws IOException {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getProject(projectId);
    }

}
