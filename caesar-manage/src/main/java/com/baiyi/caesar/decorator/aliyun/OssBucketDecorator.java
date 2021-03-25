package com.baiyi.caesar.decorator.aliyun;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
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

    @Resource
    private CsOssBucketService csOssBucketService;

    public void decorator(OssBucketVO.IBucket iBucket) {
        if (IDUtil.isEmpty(iBucket.getOssBucketId())) return;
        CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(iBucket.getOssBucketId());
        if (csOssBucket != null)
            iBucket.setBucket(BeanCopierUtil.copyProperties(csOssBucket, OssBucketVO.Bucket.class));
    }

    public OssBucketVO.Bucket decorator(OssBucketVO.Bucket bucket, Integer extend) {
        bucket.setBusinessType(BusinessType.ALIYUN_OSS_BUCKET.getType());
        tagDecorator.decorator(bucket);
        return bucket;
    }


}
