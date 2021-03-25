package com.baiyi.caesar.decorator.gitlab;

import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.vo.gitlab.GitlabProjectVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/20 2:26 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectDecorator {

    @Resource
    private GitlabInstanceDecorator gitlabInstanceDecorator;

    @Resource
    private TagDecorator tagDecorator;

    public GitlabProjectVO.Project decorator(GitlabProjectVO.Project project, Integer extend) {
        if (extend == 0) return project;

        gitlabInstanceDecorator.decorator(project);

        tagDecorator.decorator(project);       // 装饰 标签

        return project;
    }


}
