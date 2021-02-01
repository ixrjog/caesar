package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.vo.platform.BlockPlatformVO;
import com.baiyi.caesar.facade.BlockPlatformFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/1/29 9:58 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/platform")
@Api(tags = "组织架构管理")
public class PlatformController {

    @Resource
    private BlockPlatformFacade blockPlatformFacade;

    @ApiOperation(value = "查询平台封网状态")
    @GetMapping(value = "/block/status/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BlockPlatformVO.BlockPlatformStatus> queryBlockPlatformStatus() {
        return new HttpResult<>(blockPlatformFacade.queryBlockPlatformStatus());
    }

}
