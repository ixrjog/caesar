package com.baiyi.caesar.builder.gitlab;

import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import org.gitlab.api.models.GitlabBranchCommit;

/**
 * @Author baiyi
 * @Date 2020/12/2 1:37 下午
 * @Version 1.0
 */
public class GitlabBaseBranchBuilder {

    public static GitlabBranchVO.BaseBranch build(CsGitlabProject csGitlabProject, GitlabBranchCommit gitlabBranchCommit,String branch) {
        // https://gitlab.xinc818.com/android/vip8/-/commit/a0cdbc30199c1d15b16a3852e56048f4d0d71a3a
        // https://gitlab.xinc818.com/hanghaosai/xcv-cli
        return GitlabBranchVO.BaseBranch.builder()
                .commit(gitlabBranchCommit.getId())
                .commitMessage(gitlabBranchCommit.getMessage())
                .commitUrl(GitlabUtil.buildCommitUrl(csGitlabProject.getWebUrl(),gitlabBranchCommit.getId()))
                .name(branch)
                .build();
    }
}
