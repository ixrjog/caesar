package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.param.aliyun.OSSBucketParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsOssBucketMapper extends Mapper<CsOssBucket> {
    List<CsOssBucket> queryCsOssBucketByParam(OSSBucketParam.BucketPageQuery pageQuery);
}