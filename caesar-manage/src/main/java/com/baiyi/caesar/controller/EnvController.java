package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.env.EnvParam;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.facade.EnvFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:27 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/env")
@Api(tags = "环境管理")
public class EnvController {

    @Resource
    private EnvFacade envFacade;

    @ApiOperation(value = "分页查询env列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EnvVO.Env>> queryEnvPage(@Valid EnvParam.PageQuery pageQuery) {
        return new HttpResult<>(envFacade.queryEnvPage(pageQuery));
    }

    @ApiOperation(value = "新增env")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addEnv(@RequestBody @Valid EnvVO.Env env) {
        return new HttpResult<>(envFacade.addEnv(env));
    }

    @ApiOperation(value = "更新env")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateEnv(@RequestBody @Valid EnvVO.Env env) {
        return new HttpResult<>(envFacade.updateEnv(env));
    }

    @ApiOperation(value = "删除指定的env")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEnvById(@RequestParam int id) {
        return new HttpResult<>(envFacade.deleteEnvById(id));
    }
}
