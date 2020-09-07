package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CdJobParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CdJobVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.gitlab.api.models.GitlabBranchCommit;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/21 2:48 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/application")
@Api(tags = "应用配置")
public class ApplicationController {

    @Resource
    private ApplicationFacade applicationFacade;

    @ApiOperation(value = "分页查询应用配置")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryApplicationPage(@RequestBody @Valid ApplicationParam.ApplicationPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryApplicationPage(pageQuery));
    }

    @ApiOperation(value = "分页查询我的应用配置")
    @PostMapping(value = "/my/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryMyApplicationPage(@RequestBody @Valid ApplicationParam.ApplicationPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryMyApplicationPage(pageQuery));
    }

    @ApiOperation(value = "新增应用配置")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplication(@RequestBody @Valid ApplicationVO.Application application) {
        return new HttpResult<>(applicationFacade.addApplication(application));
    }

    @ApiOperation(value = "更新应用配置")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateApplication(@RequestBody @Valid ApplicationVO.Application application) {
        return new HttpResult<>(applicationFacade.updateApplication(application));
    }

    @ApiOperation(value = "删除应用配置")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteApplicationById(@RequestParam int id) {
        return new HttpResult<>(applicationFacade.deleteApplicationById(id));
    }

    @ApiOperation(value = "查询应用SCM配置")
    @GetMapping(value = "/scm/member/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ApplicationVO.ScmMember>> queryApplicationSCMMember(@Valid int applicationId) {
        return new HttpResult<>(applicationFacade.queryApplicationScmMemberByApplicationId(applicationId));
    }

    @ApiOperation(value = "查询应用SCM分支详情")
    @PostMapping(value = "/scm/member/branch/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(@RequestBody @Valid ApplicationParam.ScmMemberBranchQuery query) {
        return new HttpResult<>(applicationFacade.queryApplicationSCMMemberBranch(query));
    }

    @ApiOperation(value = "查询应用SCM分支commit详情")
    @PostMapping(value = "/scm/member/branch/commit/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<GitlabBranchCommit> queryApplicationSCMMemberBranchCommit(@RequestBody @Valid ApplicationParam.ScmMemberBranchCommitQuery query) {
        return new HttpResult<>(applicationFacade.queryApplicationSCMMemberBranchCommit(query));
    }

    @ApiOperation(value = "新增应用SCM配置")
    @PutMapping(value = "/scm/member/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationSCMMember(@Valid int applicationId, @Valid int projectId) {
        return new HttpResult<>(applicationFacade.addApplicationSCMMember(applicationId, projectId));
    }

    @ApiOperation(value = "移除应用SCM配置")
    @DeleteMapping(value = "/scm/member/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeApplicationSCMMember(@Valid int id) {
        return new HttpResult<>(applicationFacade.removeApplicationSCMMember(id));
    }

    @ApiOperation(value = "分页查询持续集成任务配置")
    @PostMapping(value = "/ci/job/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CiJobVO.CiJob>> queryCiJobPage(@RequestBody @Valid CiJobParam.CiJobPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryCiJobPage(pageQuery));
    }

    @ApiOperation(value = "分页查询持续集成任务配置")
    @PostMapping(value = "/cd/job/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CdJobVO.CdJob>> queryCdJobPage(@RequestBody @Valid CdJobParam.CdJobPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryCdJobPage(pageQuery));
    }


    @ApiOperation(value = "新增持续集成任务配置")
    @PostMapping(value = "/ci/job/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCiJob(@RequestBody @Valid CiJobVO.CiJob ciJob) {
        return new HttpResult<>(applicationFacade.addCiJob(ciJob));
    }

    @ApiOperation(value = "更新持续集成任务配置")
    @PutMapping(value = "/ci/job/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateCiJob(@RequestBody @Valid CiJobVO.CiJob ciJob) {
        return new HttpResult<>(applicationFacade.updateCiJob(ciJob));
    }

    @ApiOperation(value = "新增持续集成部署任务配置")
    @PostMapping(value = "/cd/job/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCdJob(@RequestBody @Valid CdJobVO.CdJob cdJob) {
        return new HttpResult<>(applicationFacade.addCdJob(cdJob));
    }

    @ApiOperation(value = "更新持续集成部署任务配置")
    @PutMapping(value = "/cd/job/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateCdJob(@RequestBody @Valid CdJobVO.CdJob cdJob) {
        return new HttpResult<>(applicationFacade.updateCdJob(cdJob));
    }

    @ApiOperation(value = "创建任务工作引擎")
    @PutMapping(value = "/ci/job/engine/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createCiJobEngine(@Valid int ciJobId) {
        return new HttpResult<>(applicationFacade.createJobEngine(BuildType.BUILD.getType(),ciJobId));
    }

    @ApiOperation(value = "创建任务工作引擎")
    @PutMapping(value = "/cd/job/engine/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> createCdJobEngine(@Valid int cdJobId) {
        return new HttpResult<>(applicationFacade.createJobEngine(BuildType.DEPLOYMENT.getType(),cdJobId));
    }

    @ApiOperation(value = "查询构建任务工作引擎")
    @GetMapping(value = "/ci/job/engine/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<JobEngineVO.JobEngine>> queryCiJobEngine(@Valid int ciJobId) {
        return new HttpResult<>(applicationFacade.queryJobEngine(BuildType.BUILD.getType(),ciJobId));
    }

    @ApiOperation(value = "查询部署任务工作引擎")
    @GetMapping(value = "/cd/job/engine/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<JobEngineVO.JobEngine>> queryCdJobEngine(@Valid int cdJobId) {
        return new HttpResult<>(applicationFacade.queryJobEngine(BuildType.DEPLOYMENT.getType(),cdJobId));
    }

    @ApiOperation(value = "查询应用工作引擎配置")
    @GetMapping(value = "/engine/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<ApplicationVO.Engine>> queryApplicationEngine(@Valid int applicationId) {
        return new HttpResult<>(applicationFacade.queryApplicationEngineByApplicationId(applicationId));
    }

    @ApiOperation(value = "新增应用工作引擎配置")
    @PutMapping(value = "/engine/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationEngine(@Valid int applicationId, @Valid int jenkinsInstanceId) {
        return new HttpResult<>(applicationFacade.addApplicationEngine(applicationId, jenkinsInstanceId));
    }

    @ApiOperation(value = "移除应用工作引擎配置")
    @DeleteMapping(value = "/engine/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeApplicationEngine(@Valid int id) {
        return new HttpResult<>(applicationFacade.removeApplicationEngine(id));
    }

    @ApiOperation(value = "授权应用给用户")
    @PutMapping(value = "/user/grant", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserApplication(@Valid int applicationId, @Valid int userId) {
        return new HttpResult<>(applicationFacade.grantUserApplication(applicationId, userId));
    }

    @ApiOperation(value = "撤销用户的应用授权")
    @DeleteMapping(value = "/user/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserApplication(@Valid int applicationId, @Valid int userId) {
        return new HttpResult<>(applicationFacade.revokeUserApplication(applicationId, userId));
    }

    @ApiOperation(value = "更新用户权限")
    @PutMapping(value = "/user/permission/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUserApplicationPermission(@Valid int applicationId, @Valid int userId) {
        return new HttpResult<>(applicationFacade.updateUserApplicationPermission(applicationId, userId));
    }

}
