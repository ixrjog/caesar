package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:49 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/job")
@Api(tags = "任务")
public class JobController {

    @Resource
    private JobFacade jobFacade;

    @ApiOperation(value = "执行持续集成构建任务")
    @PostMapping(value = "/ci/build", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> buildCiJob(@RequestBody @Valid JobBuildParam.CiBuildParam buildParam) {
        return new HttpResult<>(jobFacade.buildCiJob(buildParam));
    }

    @ApiOperation(value = "分页查询持续集成构建任务")
    @PostMapping(value = "/ci/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CiJobBuildVO.JobBuild>> queryCiJobBuildPage(@RequestBody @Valid JobBuildParam.JobBuildPageQuery pageQuery) {
        return new HttpResult<>(jobFacade.queryCiJobBuildPage(pageQuery));
    }


}
