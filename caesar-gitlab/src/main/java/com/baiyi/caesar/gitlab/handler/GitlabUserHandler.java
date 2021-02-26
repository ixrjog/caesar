package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2021/2/22 3:06 下午
 * @Version 1.0
 */
@Component
public class GitlabUserHandler {

    public List<GitlabUser> queryUsers(String gitlabName, String username) throws IOException {
        return Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).findUsers(username);
    }

    public void deleteGroupMember(String gitlabName, Integer userId,Integer groupId) throws IOException {
        Objects.requireNonNull(GitlabServerContainer.getGitlabAPI(gitlabName)).deleteGroupMember(groupId,  userId);
    }

}
