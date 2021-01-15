package com.baiyi.caesar.builder.gitlab;

import com.baiyi.caesar.bo.gitlab.GitlabGroupBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import org.gitlab.api.models.GitlabGroup;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:29 下午
 * @Version 1.0
 */
public class GitlabGroupBuilder {

    public static CsGitlabGroup build(CsGitlabInstance csGitlabInstance, GitlabGroup gitlabGroup) {
        // 协议跟随 Gitlab实例url转换
        String webUrl = gitlabGroup.getWebUrl();
        if (csGitlabInstance.getUrl().startsWith("https")) {
            webUrl = webUrl.replace("http", "https");
        }
        GitlabGroupBO bo = GitlabGroupBO.builder()
                .instanceId(csGitlabInstance.getId())
                .groupId(gitlabGroup.getId())
                .name(gitlabGroup.getName())
                .path(gitlabGroup.getPath())
                .description(gitlabGroup.getDescription())
                .groupVisibility(gitlabGroup.getVisibility().toString())
                .fullName(gitlabGroup.getFullName())
                .fullPath(gitlabGroup.getFullPath())
                .webUrl(webUrl)
                .build();
        return covert(bo);
    }

    private static CsGitlabGroup covert(GitlabGroupBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsGitlabGroup.class);
    }
}
