package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.aliyun.OSSBucketParam;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:13 下午
 * @Version 1.0
 */
public interface AliyunOSSFacade {

    void syncOSSBucket();

    DataTable<OssBucketVO.Bucket> queryOSSBucketPage(OSSBucketParam.BucketPageQuery pageQuery);

    BusinessWrapper<Boolean> updateOSSBucket(OssBucketVO.Bucket bucket);

    BusinessWrapper<Boolean> setBucketActiveById(int id);

    BusinessWrapper<Boolean> deleteOSSBucketById(int id);
}
