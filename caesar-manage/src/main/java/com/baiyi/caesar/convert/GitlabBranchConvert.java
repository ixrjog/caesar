package com.baiyi.caesar.convert;

import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.google.common.collect.Lists;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabTag;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/30 1:18 下午
 * @Version 1.0
 */
public class GitlabBranchConvert {

    public static GitlabBranchVO.Option build(String label, List<GitlabBranchVO.BaseBranch> branches) {
        return GitlabBranchVO.Option.builder()
                .label(label)
                .options(convert(branches))
                .build();
    }

    private static List<GitlabBranchVO.Children> convert(List<GitlabBranchVO.BaseBranch> branches) {
        return branches.stream().map(e -> {
            GitlabBranchVO.Children children = new GitlabBranchVO.Children();
            children.setLabel(e.getName());
            children.setValue(e.getName());
            return children;
        }).collect(Collectors.toList());
    }

    public static List<GitlabBranchVO.BaseBranch> convertBranches(List<GitlabBranch> branches) {
        if (CollectionUtils.isEmpty(branches))
            return Lists.newArrayList();
        return branches.stream().map(e ->
                GitlabBranchVO.BaseBranch.builder()
                        .name(e.getName())
                        .commit(e.getCommit().getId())
                        .commitMessage(e.getCommit().getMessage())
                        .build()
        ).collect(Collectors.toList());
    }

    public static List<GitlabBranchVO.BaseBranch> convertTags(List<GitlabTag> tags) {
        if (CollectionUtils.isEmpty(tags))
            return Lists.newArrayList();
        return tags.stream().map(e ->
                GitlabBranchVO.BaseBranch.builder()
                        .name(e.getName())
                        .message(e.getMessage())
                        .commit(e.getCommit().getId())
                        .commitMessage(e.getCommit().getMessage())
                        .build()
        ).collect(Collectors.toList());
    }
}
