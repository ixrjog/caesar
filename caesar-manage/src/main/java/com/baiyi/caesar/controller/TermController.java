package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.term.TermSessionParam;
import com.baiyi.caesar.domain.vo.term.TerminalSessionInstanceVO;
import com.baiyi.caesar.domain.vo.term.TerminalSessionVO;
import com.baiyi.caesar.facade.TerminalFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:10 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/term")
@Api(tags = "Web终端管理")
public class TermController {

    @Resource
    private TerminalFacade terminalFacade;

    @ApiOperation(value = "终端会话分页查询")
    @PostMapping(value = "/session/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TerminalSessionVO.TerminalSession>> querySessionPage(@RequestBody @Valid TermSessionParam.PageQuery pageQuery) {
        return new HttpResult<>(terminalFacade.queryTerminalSessionPage(pageQuery));
    }

    @ApiOperation(value = "查询会话实例详情")
    @GetMapping(value = "/session/instance/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TerminalSessionInstanceVO.TerminalSessionInstance> querySessionInstanceById(@Valid int id) {
        return new HttpResult<>(terminalFacade.querySessionInstanceById(id));
    }

}
