package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.vo.caesar.HealthVO;
import com.baiyi.caesar.facade.CaesarInstanceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/9/7 2:12 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/health")
@Api(tags = "权限配置")
public class CaesarController {

    @Resource
    private CaesarInstanceFacade caesarInstanceFacade;

    @ApiOperation(value = "负载均衡健康检查接口")
    @GetMapping(value = "/slb-health-check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<HealthVO.Health> checkHealth() {
        return new HttpResult<>(caesarInstanceFacade.checkHealth());
    }
}
