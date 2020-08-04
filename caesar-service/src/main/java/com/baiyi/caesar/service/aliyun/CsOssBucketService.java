package com.baiyi.caesar.service.aliyun;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.param.aliyun.OSSBucketParam;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:15 下午
 * @Version 1.0
 */
public interface CsOssBucketService {

    DataTable<CsOssBucket> queryCsOssBucketByParam(OSSBucketParam.BucketPageQuery pageQuery);

    CsOssBucket queryCsOssBucketByName(String name);

    CsOssBucket queryCsOssBucketById(int id);

    void addCsOssBucket(CsOssBucket csOssBucket);

    void updateCsOssBucket(CsOssBucket csOssBucket);

    void deleteCsOssBucketById(int id);
}
