package com.baiyi.caesar.aliyun.oss;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.Bucket;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.aliyun.oss.handler.AliyunOSSHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/31 3:03 下午
 * @Version 1.0
 */
public class AliyunOSSTest extends BaseUnit {

    @Resource
    private AliyunOSSHandler aliyunOSSHandler;

    @Test
    void testGetBuckets() {
        List<Bucket> buckets = aliyunOSSHandler.getBuckets();
        System.err.println(JSON.toJSONString(buckets));
    }

}
