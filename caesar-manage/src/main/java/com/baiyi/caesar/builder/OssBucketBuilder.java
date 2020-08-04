package com.baiyi.caesar.builder;

import com.aliyun.oss.model.Bucket;
import com.baiyi.caesar.bo.OssBucketBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:38 下午
 * @Version 1.0
 */
public class OssBucketBuilder {

    public static CsOssBucket build(Bucket bucket) {
        OssBucketBO bo = OssBucketBO.builder()
                .name(bucket.getName())
                .bucketLocation(bucket.getLocation())
                .extranetEndpoint(bucket.getExtranetEndpoint())
                .intranetEndpoint(bucket.getIntranetEndpoint())
                .bucketRegion(bucket.getRegion())
                .build();
        return covert(bo);
    }

    private static CsOssBucket covert(OssBucketBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsOssBucket.class);
    }
}
