package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.aliyun.CrParam;
import com.baiyi.caesar.domain.vo.aliyun.AliyunCRVO;
import com.baiyi.caesar.facade.AliyunCRFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 5:25 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/aliyun/cr")
@Api(tags = "阿里云镜像仓库配置")
public class AliyunCRController {

    @Resource
    private AliyunCRFacade aliyunCRFacade;

    @ApiOperation(value = "同步实例")
    @GetMapping(value = "/instance/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCRInstance() {
        aliyunCRFacade.syncCRInstance();
        return new HttpResult<>(BusinessWrapper.SUCCESS);
    }

    @ApiOperation(value = "分页查询实例配置")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunCRVO.Instance>> queryBucketPage(@RequestBody @Valid CrParam.InstancePageQuery pageQuery) {
        return new HttpResult<>(aliyunCRFacade.queryCRInstancePage(pageQuery));
    }

    @ApiOperation(value = "设置实例有效状态")
    @PutMapping(value = "/instance/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setInstanceActive(@Valid int id) {
        return new HttpResult<>(aliyunCRFacade.setInstanceActiveById(id));
    }

    @ApiOperation(value = "删除实例")
    @DeleteMapping(value = "/instance/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteInstanceById(@RequestParam int id) {
        return new HttpResult<>(aliyunCRFacade.deleteInstanceById(id));
    }
}
