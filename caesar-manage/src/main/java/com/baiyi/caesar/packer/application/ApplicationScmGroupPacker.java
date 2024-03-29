package com.baiyi.caesar.packer.application;

import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.service.gitlab.CsGitlabGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/8 5:19 下午
 * @Version 1.0
 */
@Component
public class ApplicationScmGroupPacker {

    @Resource
    private TagPacker tagPacker;

    @Resource
    private CsGitlabGroupService gitlabGroupService;

    public ApplicationVO.ScmGroup wrap(ApplicationVO.ScmGroup scmGroup) {
        // 装饰 标签
        CsGitlabGroup csGitlabGroup = gitlabGroupService.queryCsGitlabGroupById(scmGroup.getGroupId());
        if(csGitlabGroup != null){
            scmGroup.setName(csGitlabGroup.getName());
            scmGroup.setDescription(csGitlabGroup.getDescription());
            scmGroup.setWebUrl(csGitlabGroup.getWebUrl());
            tagPacker.wrap(scmGroup);
        }
        return scmGroup;
    }
}
