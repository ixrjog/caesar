package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.caesar.CaesarInstanceParam;
import com.baiyi.caesar.domain.vo.caesar.CaesarVO;
import com.baiyi.caesar.domain.vo.caesar.HealthVO;
import com.baiyi.caesar.facade.CaesarInstanceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/9/7 2:12 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/caesar")
@Api(tags = "凯撒管理")
public class CaesarController {

    @Resource
    private CaesarInstanceFacade caesarInstanceFacade;

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class ResourceInactiveException extends RuntimeException {
        // CaesarInstance维护模式
    }

    @ApiOperation(value = "负载均衡健康检查接口")
    @GetMapping(value = "/health/slb-health-check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<HealthVO.Health> checkHealth() {
        HealthVO.Health health = caesarInstanceFacade.checkHealth();
        if (health.isHealth()) {
            return new HttpResult<>(caesarInstanceFacade.checkHealth());
        } else {
            throw new ResourceInactiveException();
        }
    }

    @ApiOperation(value = "分页查询凯撒实例配置")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CaesarVO.Instance>> queryCaesarInstancePage(@RequestBody @Valid CaesarInstanceParam.CaesarInstancePageQuery pageQuery) {
        return new HttpResult<>(caesarInstanceFacade.queryCaesarInstancePage(pageQuery));
    }

    @ApiOperation(value = "设置凯撒实例是否有效")
    @GetMapping(value = "/instance/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCaesarInstanceActive(@RequestParam int id) {
        return new HttpResult<>(caesarInstanceFacade.setCaesarInstanceActive(id));
    }

}
