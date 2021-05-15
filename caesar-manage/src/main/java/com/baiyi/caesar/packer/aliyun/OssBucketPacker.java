package com.baiyi.caesar.packer.aliyun;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.packer.tag.TagPacker;
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
public class OssBucketPacker {

    @Resource
    private CsOssBucketService ossBucketService;

    @Resource
    private TagPacker tagPacker;

    public void wrap(OssBucketVO.IBucket iBucket) {
        if (IDUtil.isEmpty(iBucket.getOssBucketId())) return;
        CsOssBucket csOssBucket = ossBucketService.queryCsOssBucketById(iBucket.getOssBucketId());
        if (csOssBucket != null)
            iBucket.setBucket(BeanCopierUtil.copyProperties(csOssBucket, OssBucketVO.Bucket.class));
    }

    public OssBucketVO.Bucket wrap(OssBucketVO.Bucket bucket) {
        bucket.setBusinessType(BusinessType.ALIYUN_OSS_BUCKET.getType());
        tagPacker.wrap(bucket);
        return bucket;
    }


}
