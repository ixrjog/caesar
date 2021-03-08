package com.baiyi.caesar.decorator.gitlab;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabProjectVO;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
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
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private TagDecorator tagDecorator;

    public GitlabProjectVO.Project decorator(GitlabProjectVO.Project project, Integer extend) {
        if (extend == 0) return project;
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(project.getInstanceId());
        project.setInstance(BeanCopierUtil.copyProperties(csGitlabInstance, GitlabInstanceVO.Instance.class));

        // 装饰 标签
        project.setTags(tagDecorator.decorator(BusinessType.GITLAB_PROJECT.getType(), project.getId()));

        return project;
    }


}
