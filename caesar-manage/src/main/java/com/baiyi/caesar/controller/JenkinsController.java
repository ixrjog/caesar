package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.param.jenkins.JobTplParam;
import com.baiyi.caesar.domain.param.jenkins.JenkinsInstanceParam;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsJobVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.facade.JenkinsFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/17 5:46 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/jenkins")
@Api(tags = "Jenkins管理")
public class JenkinsController {

    @Resource
    private JenkinsFacade jenkinsFacade;

    @ApiOperation(value = "分页查Jenkins实例配置")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JenkinsInstanceVO.Instance>> queryJenkinsInstancePage(@RequestBody @Valid JenkinsInstanceParam.JenkinsInstancePageQuery pageQuery) {
        return new HttpResult<>(jenkinsFacade.queryJenkinsInstancePage(pageQuery));
    }

    @ApiOperation(value = "新增Jenkins实例配置")
    @PostMapping(value = "/instance/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addJenkinsInstance(@RequestBody @Valid JenkinsInstanceVO.Instance instance) {
        return new HttpResult<>(jenkinsFacade.addJenkinsInstance(instance));
    }

    @ApiOperation(value = "更新Jenkins实例配置")
    @PutMapping(value = "/instance/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateJenkinsInstance(@RequestBody @Valid JenkinsInstanceVO.Instance instance) {
        return new HttpResult<>(jenkinsFacade.updateJenkinsInstance(instance));
    }

    @ApiOperation(value = "删除Jenkins实例配置")
    @DeleteMapping(value = "/instance/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteJenkinsInstanceById(@RequestParam int id) {
        return new HttpResult<>(jenkinsFacade.deleteJenkinsInstanceById(id));
    }

    @ApiOperation(value = "查任实例任务模版")
    @GetMapping(value = "/tpl/instance/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<JenkinsJobVO.Job>> queryJobTplByInstanceId(@Valid int instanceId) {
        return new HttpResult<>(jenkinsFacade.queryJobTplByInstanceId(instanceId));
    }

    @ApiOperation(value = "分页查询任务模版配置")
    @PostMapping(value = "/tpl/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<JobTplVO.JobTpl>> queryJobTplPage(@RequestBody @Valid JobTplParam.JobTplPageQuery pageQuery) {
        return new HttpResult<>(jenkinsFacade.queryJobTplPage(pageQuery));
    }

    @ApiOperation(value = "新增任务模版配置")
    @PostMapping(value = "/tpl/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addJobTpl(@RequestBody @Valid JobTplVO.JobTpl jobTpl) {
        return new HttpResult<>(jenkinsFacade.addJobTpl(jobTpl));
    }

    @ApiOperation(value = "更新任务模版配置")
    @PutMapping(value = "/tpl/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateJobTpl(@RequestBody @Valid JobTplVO.JobTpl jobTpl) {
        return new HttpResult<>(jenkinsFacade.updateJobTpl(jobTpl));
    }

    @ApiOperation(value = "删除任务模版配置")
    @DeleteMapping(value = "/tpl/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteJobTplById(@RequestParam int id) {
        return new HttpResult<>(jenkinsFacade.deleteJobTplById(id));
    }

    @ApiOperation(value = "读取任务模版配置")
    @GetMapping(value = "/tpl/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> readJobTplById(@Valid int id) {
        return new HttpResult<>(jenkinsFacade.readJobTplById(id));
    }

}
