package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.common.util.JenkinsUtils;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.application.CdJobVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/31 2:23 下午
 * @Version 1.0
 */
@Component
public class CdJobDecorator {

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private JobDeploymentDecorator jobDeploymentDecorator;

    public CdJobVO.CdJob decorator(CsCdJob csCdJob) {
        return decorator(BeanCopierUtils.copyProperties(csCdJob, CdJobVO.CdJob.class));
    }

    public CdJobVO.CdJob decorator(CdJobVO.CdJob cdJob) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(cdJob.getCiJobId());
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(csCiJob.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            cdJob.setEnv(env);
        }

        if (!IDUtils.isEmpty(cdJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(cdJob.getJobTplId());
            if (csJobTpl != null)
                cdJob.setJobTpl(BeanCopierUtils.copyProperties(csJobTpl, JobTplVO.JobTpl.class));
        }

        // 参数
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtils.convert(cdJob.getParameterYaml());
        Map<String, String> params = JenkinsUtils.convert(jenkinsJobParameters);
        cdJob.setParameters(params);


        JobDeploymentParam.DeploymentPageQuery query = new JobDeploymentParam.DeploymentPageQuery();
        query.setCdJobId(cdJob.getId());
        query.setExtend(1);
        cdJob.setBuildViews(acqCdJobBuildView(query));

        return cdJob;
    }

    private List<CdJobBuildVO.JobBuildView> acqCdJobBuildView(JobDeploymentParam.DeploymentPageQuery pageQuery) {
        pageQuery.setPage(1);
        pageQuery.setLength(3);
        DataTable<CsCdJobBuild> table = csCdJobBuildService.queryCdJobBuildPage(pageQuery);
        List<CsCdJobBuild> builds = table.getData();
        return builds.stream().map(e -> {
            CdJobBuildVO.JobBuildView jobBuildView = new CdJobBuildVO.JobBuildView();
            jobBuildView.setBuildNumber(e.getJobBuildNumber());
            jobBuildView.setBuilding(!e.getFinalized());
            if (!e.getFinalized()) {
                jobBuildView.setColor("#E07D06");
            } else {
                if ("SUCCESS".equals(e.getBuildStatus())) {
                    jobBuildView.setColor("#17BA14");
                } else {
                    jobBuildView.setColor("#DD3E03");
                }
            }
            jobBuildView.setExecutors(jobDeploymentDecorator.getBuildExecutorByBuildId(e.getId()));
            return jobBuildView;
        }).collect(Collectors.toList());
    }

}
