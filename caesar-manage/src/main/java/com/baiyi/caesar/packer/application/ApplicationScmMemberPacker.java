package com.baiyi.caesar.packer.application;

import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/22 5:42 下午
 * @Version 1.0
 */
@Component
public class ApplicationScmMemberPacker {

    @Resource
    private TagPacker tagPacker;

    public ApplicationVO.ScmMember wrap(ApplicationVO.ScmMember scmMember) {
        // 装饰 标签
        tagPacker.wrap(scmMember);
        return scmMember;
    }
}
