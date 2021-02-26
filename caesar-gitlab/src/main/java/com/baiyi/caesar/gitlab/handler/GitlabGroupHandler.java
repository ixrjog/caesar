package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabGroupMember;
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

    public GitlabGroup getGroup(String gitlabName, Integer groupId) throws IOException {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getGroup(groupId);
    }

    public List<GitlabNamespace> getNamespace(String gitlabName) {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getNamespaces();
    }

    public void addGroupMember(String gitlabName, Integer groupId, Integer userId, GitlabAccessLevel accessLevel) throws IOException {
        Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).addGroupMember(groupId, userId, accessLevel);
    }

    public List<GitlabGroupMember> getGroupMembers(String gitlabName, Integer groupId) {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).getGroupMembers(groupId);
    }
}
