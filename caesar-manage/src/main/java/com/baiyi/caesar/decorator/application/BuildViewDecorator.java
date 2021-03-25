package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.decorator.base.BaseDecorator;
import com.baiyi.caesar.decorator.jenkins.base.BaseJenkinsDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.build.BuildViewVO;
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
public class BuildViewDecorator extends BaseJenkinsDecorator {

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    public void decorator(CiJobBuildVO.IBuildView iBuildView) {
        JobBuildParam.BuildPageQuery query = new JobBuildParam.BuildPageQuery();
        query.setCiJobId(iBuildView.getCiJobId());
        query.setExtend(1);
        query.setPage(1);
        query.setLength(3);
        DataTable<CsCiJobBuild> table = csCiJobBuildService.queryCiJobBuildPage(query);
        iBuildView.setBuildViews(convertCiBuildViews(table.getData()));
    }

    public void decorator(CdJobBuildVO.IBuildView iBuildView) {
        JobDeploymentParam.DeploymentPageQuery query = new JobDeploymentParam.DeploymentPageQuery();
        query.setCdJobId(iBuildView.getCdJobId());
        query.setExtend(1);
        query.setPage(1);
        query.setLength(3);
        DataTable<CsCdJobBuild> table = csCdJobBuildService.queryCdJobBuildPage(query);
        iBuildView.setBuildViews(convertCdBuildViews(table.getData()));
    }

    private List<BuildViewVO.JobBuildView> convertCiBuildViews(List<CsCiJobBuild> builds) {
        return builds.stream().map(e -> {
                    BuildViewVO.JobBuildView vo = BuildViewVO.JobBuildView.builder()
                            .buildNumber(e.getJobBuildNumber())
                            .building(!e.getFinalized())
                            .buildType(BuildType.BUILD.getType())
                            .buildId(e.getId())
                            .color(BaseDecorator.acqBuildViewColor(e.getFinalized(), e.getBuildStatus()))
                            .build();
                    decoratorBuildExecutors(vo);
                    return vo;
                }
        ).collect(Collectors.toList());
    }

    private List<BuildViewVO.JobBuildView> convertCdBuildViews(List<CsCdJobBuild> builds) {
        return builds.stream().map(e -> {
                    BuildViewVO.JobBuildView vo = BuildViewVO.JobBuildView.builder()
                            .buildNumber(e.getJobBuildNumber())
                            .building(!e.getFinalized())
                            .buildType(BuildType.DEPLOYMENT.getType())
                            .buildId(e.getId())
                            .color(BaseDecorator.acqBuildViewColor(e.getFinalized(), e.getBuildStatus()))
                            .build();
                    decoratorBuildExecutors(vo);
                    return vo;
                }
        ).collect(Collectors.toList());
    }

}
