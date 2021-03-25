package com.baiyi.caesar.decorator.gitlab;

import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.vo.gitlab.GitlabGroupVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/21 5:49 下午
 * @Version 1.0
 */
@Component
public class GitlabGroupDecorator {

    @Resource
    private GitlabInstanceDecorator gitlabInstanceDecorator;

    @Resource
    private TagDecorator tagDecorator;

    public GitlabGroupVO.Group decorator(GitlabGroupVO.Group group, Integer extend) {
        if (extend == 0) return group;

        gitlabInstanceDecorator.decorator(group);

        // 装饰 标签
        tagDecorator.decorator(group);

        return group;
    }
}
