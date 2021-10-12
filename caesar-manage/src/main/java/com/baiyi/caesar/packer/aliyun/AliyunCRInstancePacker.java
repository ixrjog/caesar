package com.baiyi.caesar.packer.aliyun;

import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.vo.aliyun.AliyunCRVO;
import com.baiyi.caesar.packer.tag.TagPacker;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 8:06 下午
 * @Since 1.0
 */

@Component
public class AliyunCRInstancePacker {

    @Resource
    private TagPacker tagPacker;

    public AliyunCRVO.Instance wrap(AliyunCRVO.Instance instance) {
        instance.setBusinessType(BusinessType.ALIYUN_CR_INSTANCE.getType());
        tagPacker.wrap(instance);
        return instance;
    }
}
