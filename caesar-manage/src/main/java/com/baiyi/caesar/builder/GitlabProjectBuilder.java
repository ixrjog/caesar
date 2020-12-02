package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.GitlabProjectBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

/**
 * @Author baiyi
 * @Date 2020/7/20 11:46 上午
 * @Version 1.0
 */
public class GitlabProjectBuilder {

    public static CsGitlabProject build(CsGitlabInstance csGitlabInstance, GitlabProject gitlabProject) {
        // 协议跟随 Gitlab实例url转换
        String webUrl = gitlabProject.getWebUrl();
        String httpUrl = gitlabProject.getHttpUrl();
        if (csGitlabInstance.getUrl().startsWith("https")) {
            webUrl = webUrl.replace("http", "https");
            httpUrl = httpUrl.replace("http", "https");
        }
        GitlabNamespace namespace = gitlabProject.getNamespace();
        GitlabProjectBO bo = GitlabProjectBO.builder()
                .instanceId(csGitlabInstance.getId())
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
                .webUrl(webUrl)
                .httpUrl(httpUrl)
                .defaultBranch(gitlabProject.getDefaultBranch())
                .build();
        return covert(bo);
    }

    private static CsGitlabProject covert(GitlabProjectBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsGitlabProject.class);
    }
}
