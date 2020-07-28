package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.GitlabProjectBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

/**
 * @Author baiyi
 * @Date 2020/7/20 11:46 上午
 * @Version 1.0
 */
public class GitlabProjectBuilder {

    public static CsGitlabProject build(Integer instanceId, GitlabProject gitlabProject) {
        GitlabNamespace namespace = gitlabProject.getNamespace();
        GitlabProjectBO bo = GitlabProjectBO.builder()
                .instanceId(instanceId)
                .name(gitlabProject.getName())
                .projectId(gitlabProject.getId())
                .path(gitlabProject.getPath())
                .projectVisibility(gitlabProject.getVisibility())
                .namespaceId(namespace.getId())
                .namespaceName(namespace.getName())
                .namespacePath(namespace.getPath())
                .namespaceFullPath(namespace.getFullPath())
                .namespaceKind(namespace.getKind())
                .description(gitlabProject.getDescription())
                .sshUrl(gitlabProject.getSshUrl())
                .webUrl(gitlabProject.getWebUrl())
                .httpUrl(gitlabProject.getHttpUrl())
                .defaultBranch(gitlabProject.getDefaultBranch())
                .build();
        return covert(bo);
    }

    private static CsGitlabProject covert(GitlabProjectBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsGitlabProject.class);
    }
}
