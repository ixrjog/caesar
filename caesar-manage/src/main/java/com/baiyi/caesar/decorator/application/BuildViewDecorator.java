package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.decorator.application.base.BaseJobDecorator;
import com.baiyi.caesar.decorator.jenkins.JobBuildDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/17 3:02 下午
 * @Version 1.0
 */
@Component
public class BuildViewDecorator extends BaseJobDecorator {

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private JobBuildDecorator jobBuildDecorator;

    @Resource
    private JobDeploymentDecorator jobDeploymentDecorator;

    public void decorator(CiJobBuildVO.IBuildView iBuildView) {
        JobBuildParam.BuildPageQuery query = new JobBuildParam.BuildPageQuery();
        query.setCiJobId(iBuildView.getCiJobId());
        query.setExtend(1);
        iBuildView.setBuildViews(acqCiJobBuildView(query));
    }

    public void decorator(CdJobBuildVO.IBuildView iBuildView) {
        JobDeploymentParam.DeploymentPageQuery query = new JobDeploymentParam.DeploymentPageQuery();
        query.setCdJobId(iBuildView.getCdJobId());
        query.setExtend(1);
        iBuildView.setBuildViews(acqCdJobBuildView(query));
    }

    private List<CiJobBuildVO.JobBuildView> acqCiJobBuildView(JobBuildParam.BuildPageQuery pageQuery) {
        pageQuery.setPage(1);
        pageQuery.setLength(3);
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(pageQuery);
        List<CsCiJobBuild> builds = table.getData();
        return builds.stream().map(e -> {
            CiJobBuildVO.JobBuildView jobBuildView = new CiJobBuildVO.JobBuildView();
            jobBuildView.setBuildNumber(e.getJobBuildNumber());
            jobBuildView.setBuilding(!e.getFinalized());
            assembleJobBuildView(jobBuildView, e.getFinalized(), e.getBuildStatus());
            jobBuildView.setExecutors(jobBuildDecorator.getBuildExecutorByBuildId(e.getId()));
            return jobBuildView;
        }).collect(Collectors.toList());
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
            assembleJobBuildView(jobBuildView, e.getFinalized(), e.getBuildStatus());
            jobBuildView.setExecutors(jobDeploymentDecorator.getExecutorsByBuildId(e.getId()));
            return jobBuildView;
        }).collect(Collectors.toList());
    }
}
