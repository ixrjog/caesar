package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.dingtalk.DingtalkParam;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.facade.DingtalkFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:21 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/dingtalk")
@Api(tags = "钉钉配置")
public class DingtalkController {

    @Resource
    private DingtalkFacade dingtalkFacade;

    @ApiOperation(value = "分页查询Dingtalk配置")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DingtalkVO.Dingtalk>> queryDingtalkPage(@RequestBody @Valid DingtalkParam.DingtalkPageQuery pageQuery) {
        return new HttpResult<>(dingtalkFacade.queryDingtalkPage(pageQuery));
    }

    @ApiOperation(value = "新增Dingtalk配置")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDingtalk(@RequestBody @Valid DingtalkVO.Dingtalk dingtalk) {
        return new HttpResult<>(dingtalkFacade.addDingtalk(dingtalk));
    }

    @ApiOperation(value = "更新Dingtalk配置")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDingtalk(@RequestBody @Valid DingtalkVO.Dingtalk dingtalk) {
        return new HttpResult<>(dingtalkFacade.updateDingtalk(dingtalk));
    }

    @ApiOperation(value = "测试Dingtalk配置")
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> testDingtalkById(@Valid int id) {
        return new HttpResult<>(dingtalkFacade.testDingtalkById(id));
    }

    @ApiOperation(value = "删除Dingtalk配置")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteDingtalkById(@RequestParam int id) {
        return new HttpResult<>(dingtalkFacade.deleteDingtalkById(id));
    }

}
