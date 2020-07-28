package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/22 5:42 下午
 * @Version 1.0
 */
@Component
public class ApplicationScmMemberDecorator {

    @Resource
    private TagDecorator tagDecorator;

    public ApplicationVO.ScmMember decorator(ApplicationVO.ScmMember scmMember, Integer extend) {
        // 装饰 标签
        scmMember.setTags(tagDecorator.decorator(BusinessType.GITLAB_PROJECT.getType(), scmMember.getScmId()));
        return scmMember;
    }
}
