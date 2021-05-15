package com.baiyi.caesar.packer.gitlab;

import com.baiyi.caesar.packer.base.BaseDecorator;
import com.baiyi.caesar.domain.vo.gitlab.GitlabGroupVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/10/21 5:49 下午
 * @Version 1.0
 */
@Component
public class GitlabGroupDecorator extends BaseDecorator {

    public GitlabGroupVO.Group decorator(GitlabGroupVO.Group group, Integer extend) {
        if (extend == 0) return group;
        decoratorBaseGitlab(group);
        return group;
    }
}
