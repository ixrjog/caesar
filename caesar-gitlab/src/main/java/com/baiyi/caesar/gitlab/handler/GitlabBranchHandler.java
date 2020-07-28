package com.baiyi.caesar.gitlab.handler;

import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabTag;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 6:41 下午
 * @Version 1.0
 */
@Component
public class GitlabBranchHandler {

    public List<GitlabBranch> getBranchs(String gitlabName, Integer projectId) {
        return GitlabServerContainer.getGitlabAPI(gitlabName).getBranches(projectId);
    }

    public List<GitlabTag> getTags(String gitlabName, Integer projectId) {
        return GitlabServerContainer.getGitlabAPI(gitlabName).getTags(projectId);
    }
}
