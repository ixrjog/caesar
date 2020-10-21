package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.gitlab.GitlabGroupBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import org.gitlab.api.models.GitlabGroup;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:29 下午
 * @Version 1.0
 */
public class GitlabGroupBuilder {

    public static CsGitlabGroup build(int instanceId, GitlabGroup gitlabGroup) {

        GitlabGroupBO bo = GitlabGroupBO.builder()
                .instanceId(instanceId)
                .groupId(gitlabGroup.getId())
                .name(gitlabGroup.getName())
                .path(gitlabGroup.getPath())
                .description(gitlabGroup.getDescription())
                .groupVisibility(gitlabGroup.getVisibility().toString())
                .fullName(gitlabGroup.getFullName())
                .fullPath(gitlabGroup.getFullPath())
                .webUrl(gitlabGroup.getWebUrl())
                .build();
        return covert(bo);
    }

    private static CsGitlabGroup covert(GitlabGroupBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsGitlabGroup.class);
    }
}
