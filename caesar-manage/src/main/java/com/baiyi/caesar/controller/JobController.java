package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupHostPatternVO;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
    public HttpResult<Boolean> buildCiJob(@RequestBody @Valid JobBuildParam.BuildParam buildParam) {
        return new HttpResult<>(jobFacade.buildCiJob(buildParam));
    }

    @ApiOperation(value = "中止持续集成构建任务")
    @PutMapping(value = "/ci/build/abort",  produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> abortBuildCiJob(@Valid int ciBuildId) {
        return new HttpResult<>(jobFacade.abortCiJobBuild(ciBuildId));
    }

    @ApiOperation(value = "执行持续集成部署任务")
    @PostMapping(value = "/cd/build", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> buildCdJob(@RequestBody @Valid JobDeploymentParam.DeploymentParam deploymentParam) {
        return new HttpResult<>(jobFacade.buildCdJob(deploymentParam));
    }

    @ApiOperation(value = "分页查询持续集成构建任务")
    @PostMapping(value = "/ci/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CiJobBuildVO.JobBuild>> queryCiJobBuildPage(@RequestBody @Valid JobBuildParam.BuildPageQuery pageQuery) {
        return new HttpResult<>(jobFacade.queryCiJobBuildPage(pageQuery));
    }

    @ApiOperation(value = "分页查询持续集成部署任务")
    @PostMapping(value = "/cd/build/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CdJobBuildVO.JobBuild>> queryCdJobBuildPage(@RequestBody @Valid JobDeploymentParam.DeploymentPageQuery pageQuery) {
        return new HttpResult<>(jobFacade.queryCdJobBuildPage(pageQuery));
    }

    @ApiOperation(value = "查询持续集成构件")
    @PostMapping(value = "/ci/build/artifact/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<CiJobBuildVO.JobBuild>> queryCiJobBuildArtifact(@RequestBody @Valid JobBuildParam.JobBuildArtifactQuery query) {
        return new HttpResult<>(jobFacade.queryCiJobBuildArtifact(query));
    }

    @ApiOperation(value = "查询持续集成构建任务")
    @GetMapping(value = "/ci/build/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<CiJobBuildVO.JobBuild> queryCiJobBuildByBuildId(@Valid int buildId) {
        return new HttpResult<>(jobFacade.queryCiJobBuildByBuildId(buildId));
    }

    @ApiOperation(value = "查询持续集成构建任务")
    @GetMapping(value = "/cd/build/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<CdJobBuildVO.JobBuild> queryCdJobBuildByBuildId(@Valid int buildId) {
        return new HttpResult<>(jobFacade.queryCdJobBuildByBuildId(buildId));
    }

    @ApiOperation(value = "查询主机分组模式")
    @GetMapping(value = "/cd/host/pattern/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ServerGroupHostPatternVO.HostPattern>> queryCdJobHostPatternByJobId(@Valid int cdJobId) {
        return new HttpResult<>(jobFacade.queryCdJobHostPatternByJobId(cdJobId));
    }

    @ApiOperation(value = "查询持续集成构建任务日志")
    @PostMapping(value = "/build/output/view", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> viewJobBuildOutput(@RequestBody @Valid JobBuildParam.ViewJobBuildOutputQuery query) {
        return new HttpResult<>(jobFacade.viewJobBuildOutput(query));
    }

    @ApiOperation(value = "校正任务构建引擎")
    @GetMapping(value = "/ci/engine/correction", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> correctionJobBuildEngineByJobId(@Valid int ciJobId) {
        return new HttpResult<>(jobFacade.correctionJobEngine(BuildType.BUILD.getType(),ciJobId));
    }

    @ApiOperation(value = "校正任务部署引擎")
    @GetMapping(value = "/cd/engine/correction", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> correctionJobDeployEngineByJobId(@Valid int cdJobId) {
        return new HttpResult<>(jobFacade.correctionJobEngine(BuildType.BUILD.getType(),cdJobId));
    }

}
