package com.baiyi.caesar.decorator.gitlab;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.vo.gitlab.GitlabGroupVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
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
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private TagDecorator tagDecorator;

    public GitlabGroupVO.Group decorator(GitlabGroupVO.Group group, Integer extend) {
        if (extend == 0) return group;
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(group.getInstanceId());
        group.setInstance(BeanCopierUtil.copyProperties(csGitlabInstance, GitlabInstanceVO.Instance.class));

        // 装饰 标签
        group.setBusinessType(BusinessType.GITLAB_GROUP.getType());
        tagDecorator.decorator(group);

        // 装饰 标签
        // group.setTags(tagDecorator.decorator(BusinessType.GITLAB_GROUP.getType(), group.getId()));

        return group;
    }
}
