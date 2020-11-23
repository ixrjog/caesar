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
        List<GitlabBranchVO.Children> options = branches.stream().map(e -> {
            GitlabBranchVO.Children children = new GitlabBranchVO.Children();
            children.setLabel(e.getName());
            children.setValue(e.getName());
            return children;
        }).collect(Collectors.toList());
        GitlabBranchVO.Option option = new GitlabBranchVO.Option();
        option.setLabel(label);
        option.setOptions(options);
        return option;
    }

    public static List<GitlabBranchVO.BaseBranch> convertBranches(List<GitlabBranch> branches) {
        if (CollectionUtils.isEmpty(branches))
            return Lists.newArrayList();
        return branches.stream().map(e -> {
            GitlabBranchVO.Branch branch = new GitlabBranchVO.Branch();
            branch.setName(e.getName());
            branch.setCommit(e.getCommit().getId());
            branch.setCommitMessage(e.getCommit().getMessage());
            return branch;
        }).collect(Collectors.toList());
    }

    public static List<GitlabBranchVO.BaseBranch> convertTags(List<GitlabTag> tags) {
        if (CollectionUtils.isEmpty(tags))
            return Lists.newArrayList();
        return tags.stream().map(e -> {
            GitlabBranchVO.Tag tag = new GitlabBranchVO.Tag();
            tag.setName(e.getName());
            tag.setMessage(e.getMessage());
            tag.setCommit(e.getCommit().getId());
            tag.setCommitMessage(e.getCommit().getMessage());
            return tag;
        }).collect(Collectors.toList());
    }
}
