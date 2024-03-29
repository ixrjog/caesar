package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.aliyun.oss.handler.AliyunOSSHandler;
import com.baiyi.caesar.builder.OssBucketBuilder;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.packer.aliyun.OssBucketPacker;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.param.aliyun.OSSBucketParam;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.facade.AliyunOSSFacade;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:13 下午
 * @Version 1.0
 */
@Service
public class AliyunOSSFacadeImpl implements AliyunOSSFacade {

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    private OssBucketPacker ossBucketDecorator;

    @Resource
    private AliyunOSSHandler aliyunOSSHandler;

    @Override
    public DataTable<OssBucketVO.Bucket> queryOSSBucketPage(OSSBucketParam.BucketPageQuery pageQuery) {
        DataTable<CsOssBucket> table = csOssBucketService.queryCsOssBucketByParam(pageQuery);
        List<OssBucketVO.Bucket> page = BeanCopierUtil.copyListProperties(table.getData(), OssBucketVO.Bucket.class);
        return new DataTable<>(page.stream().map(e -> ossBucketDecorator.wrap(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> updateOSSBucket(OssBucketVO.Bucket bucket) {
        CsOssBucket csOssBucket = BeanCopierUtil.copyProperties(bucket, CsOssBucket.class);
        csOssBucketService.addCsOssBucket(csOssBucket);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setBucketActiveById(int id) {
        CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(id);
        csOssBucket.setIsActive(!csOssBucket.getIsActive());
        csOssBucketService.updateCsOssBucket(csOssBucket);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    @Async(value = Global.TaskPools.COMMON)
    public void syncOSSBucket() {
        aliyunOSSHandler.getBuckets().parallelStream().forEach(e -> {
            if (csOssBucketService.queryCsOssBucketByName(e.getName()) == null)
                csOssBucketService.addCsOssBucket(OssBucketBuilder.build(e));
        });
    }

    @Override
    public BusinessWrapper<Boolean> deleteOSSBucketById(int id) {
        csOssBucketService.deleteCsOssBucketById(id);
        return BusinessWrapper.SUCCESS;
    }

}
