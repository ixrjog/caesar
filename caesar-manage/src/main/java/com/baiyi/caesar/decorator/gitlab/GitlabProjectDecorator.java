package com.baiyi.caesar.decorator.gitlab;

import com.baiyi.caesar.decorator.base.BaseDecorator;
import com.baiyi.caesar.domain.vo.gitlab.GitlabProjectVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/7/20 2:26 下午
 * @Version 1.0
 */
@Component
public class GitlabProjectDecorator extends BaseDecorator {

    public GitlabProjectVO.Project decorator(GitlabProjectVO.Project project, Integer extend) {
        if (extend == 0) return project;
        decoratorBaseGitlab(project);
        return project;
    }


}
