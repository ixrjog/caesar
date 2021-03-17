package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.common.util.JenkinsUtil;
import com.baiyi.caesar.decorator.application.base.BaseJobDecorator;
import com.baiyi.caesar.decorator.env.EnvDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.application.CdJobVO;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/31 2:23 下午
 * @Version 1.0
 */
@Component
public class CdJobDecorator extends BaseJobDecorator {

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private EnvDecorator envDecorator;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private JobDeploymentDecorator jobDeploymentDecorator;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    public CdJobVO.CdJob decorator(CdJobVO.CdJob cdJob, CsJobTpl csJobTpl) {
        List<CsJobEngine> csCdJobEngines = csJobEngineService.queryCsJobEngineByJobId(BuildType.DEPLOYMENT.getType(), cdJob.getId());
        AtomicReference<Boolean> needUpgrade = new AtomicReference<>(false);
        if (!CollectionUtils.isEmpty(csCdJobEngines)) {
            cdJob.setJobEngines(
                    csCdJobEngines.stream().map(e -> {
                        JobEngineVO.JobEngine jobEngine = BeanCopierUtil.copyProperties(e, JobEngineVO.JobEngine.class);
                        jobEngine.setNeedUpgrade(csJobTpl.getTplVersion() > jobEngine.getTplVersion());
                        if (jobEngine.getNeedUpgrade())
                            needUpgrade.set(true);
                        return jobEngineDecorator.decorator(jobEngine);
                    }).collect(Collectors.toList())
            );
        }
        cdJob.setNeedUpgrade(needUpgrade.get());
        return cdJob;
    }


    public CdJobVO.CdJob decorator(CsCdJob csCdJob) {
        return decorator(BeanCopierUtil.copyProperties(csCdJob, CdJobVO.CdJob.class));
    }

    public CdJobVO.CdJob decorator(CdJobVO.CdJob cdJob) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(cdJob.getCiJobId());
        cdJob.setEnvType(csCiJob.getEnvType());
        // 装饰 环境信息
        envDecorator.decorator(cdJob);

        if (!IDUtil.isEmpty(cdJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(cdJob.getJobTplId());
            if (csJobTpl != null)
                cdJob.setJobTpl(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class));
        }

        // 参数
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtil.convert(cdJob.getParameterYaml());
        Map<String, String> params = JenkinsUtil.convert(jenkinsJobParameters);
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

            assembleJobBuildView(jobBuildView, e.getFinalized(), e.getBuildStatus());

            jobBuildView.setExecutors(jobDeploymentDecorator.getExecutorsByBuildId(e.getId()));
            return jobBuildView;
        }).collect(Collectors.toList());
    }

}
