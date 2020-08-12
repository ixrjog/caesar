package com.baiyi.caesar.aliyun.oss.handler;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.baiyi.caesar.aliyun.core.config.AliyunCoreConfig;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/31 2:31 下午
 * @Version 1.0
 */
@Component
public class AliyunOSSHandler {

    private static final int MAX_KEYS = 100;

    @Resource
    private AliyunCoreConfig aliyunCoreConfig;

    /**
     * 查询账户下所有用户
     *
     * @return
     */
    public List<Bucket> getBuckets() {
        List<Bucket> buckets = Lists.newArrayList();
        String marker = "";
        while (true) {
            BucketList bucketList = listBuckets(marker);
            if (CollectionUtils.isEmpty(bucketList.getBucketList()))
                return buckets;
            buckets.addAll(bucketList.getBucketList());
            marker = bucketList.getNextMarker();
            if (bucketList.getBucketList().size() < MAX_KEYS)
                return buckets;
        }
    }

    private BucketList listBuckets(String marker) {
        ListBucketsRequest request = new ListBucketsRequest();
        request.setMaxKeys(MAX_KEYS);
        if (!StringUtils.isEmpty(marker))
            request.setMarker(marker);
        try {
            OSS client = acqOSSClient();
            return client.listBuckets(request);
        } catch (OSSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OSSObjectSummary> listObjects(String bucket, String prefix) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setPrefix(prefix);
        OSS client = acqOSSClient();
        try {
            ObjectListing reponse = client.listObjects(bucket, prefix);
            if (reponse != null) return reponse.getObjectSummaries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private OSS acqOSSClient() {
        AliyunCoreConfig.Account account = aliyunCoreConfig.getAccount();
        AliyunCoreConfig.OSS oss = aliyunCoreConfig.getOss();
        return new OSSClientBuilder()
                .build(oss.getEndpoint(), account.getAccessKeyId(), account.getSecret());
    }

}
