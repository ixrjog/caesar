package com.baiyi.caesar.decorator.application;

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
public class ApplicationScmMemberWrap {

    @Resource
    private TagDecorator tagDecorator;

    public ApplicationVO.ScmMember wrap(ApplicationVO.ScmMember scmMember) {
        // 装饰 标签
        tagDecorator.decorator(scmMember);
        return scmMember;
    }
}
