package com.baiyi.caesar.decorator.aliyun;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:26 下午
 * @Version 1.0
 */
@Component
public class OssBucketDecorator {

    @Resource
    private TagDecorator tagDecorator;

    public OssBucketVO.Bucket decorator(OssBucketVO.Bucket bucket, Integer extend) {
        bucket.setTags(tagDecorator.decorator(BusinessType.ALIYUN_OSS_BUCKET.getType(), bucket.getId()));
        return bucket;
    }
}
