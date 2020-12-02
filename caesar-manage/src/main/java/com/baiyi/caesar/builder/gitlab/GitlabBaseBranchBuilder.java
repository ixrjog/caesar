package com.baiyi.caesar.builder.gitlab;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.google.common.base.Joiner;
import org.gitlab.api.models.GitlabBranchCommit;

/**
 * @Author baiyi
 * @Date 2020/12/2 1:37 下午
 * @Version 1.0
 */
public class GitlabBaseBranchBuilder {

    private static final String COMMIT_BASE = "-/commit";

    public static GitlabBranchVO.BaseBranch build(CsGitlabProject csGitlabProject, GitlabBranchCommit gitlabBranchCommit,String branch) {
        GitlabBranchVO.BaseBranch baseBranch = new GitlabBranchVO.BaseBranch();
        baseBranch.setCommit(gitlabBranchCommit.getId());
        baseBranch.setCommitMessage(gitlabBranchCommit.getMessage());
        // https://gitlab.xinc818.com/android/vip8/-/commit/a0cdbc30199c1d15b16a3852e56048f4d0d71a3a
        // https://gitlab.xinc818.com/hanghaosai/xcv-cli
        String url = Joiner.on("/").join(csGitlabProject.getWebUrl(), COMMIT_BASE, gitlabBranchCommit.getId());
        baseBranch.setCommitUrl(url);
        baseBranch.setName(branch);
        return baseBranch;
    }
}
