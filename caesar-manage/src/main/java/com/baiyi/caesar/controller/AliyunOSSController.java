package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.aliyun.OSSBucketParam;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.facade.AliyunOSSFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:11 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/aliyun/oss")
@Api(tags = "应用配置")
public class AliyunOSSController {

    @Resource
    private AliyunOSSFacade aliyunOSSFacade;

    @ApiOperation(value = "同步Bucket配置")
    @PutMapping(value = "/bucket/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncBucket() {
        aliyunOSSFacade.syncOSSBucket();
        return new HttpResult<>(BusinessWrapper.SUCCESS);
    }

    @ApiOperation(value = "分页查询Bucket配置")
    @PostMapping(value = "/bucket/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OssBucketVO.Bucket>> queryBucketPage(@RequestBody @Valid OSSBucketParam.BucketPageQuery pageQuery) {
        return new HttpResult<>(aliyunOSSFacade.queryOSSBucketPage(pageQuery));
    }

    @ApiOperation(value = "更新Bucket配置")
    @PutMapping(value = "/bucket/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBucket(@RequestBody @Valid OssBucketVO.Bucket bucket) {
        return new HttpResult<>(aliyunOSSFacade.updateOSSBucket(bucket));
    }

    @ApiOperation(value = "设置Bucket有效状态")
    @PutMapping(value = "/bucket/active/set",  produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setBucketActive( @Valid int id) {
        return new HttpResult<>(aliyunOSSFacade.setBucketActiveById(id));
    }

    @ApiOperation(value = "删除Bucket配置")
    @DeleteMapping(value = "/bucket/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteBucketById(@RequestParam int id) {
        return new HttpResult<>(aliyunOSSFacade.deleteOSSBucketById(id));
    }
}
