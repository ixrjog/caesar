package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.application.base.BaseJobDecorator;
import com.baiyi.caesar.decorator.env.EnvDecorator;
import com.baiyi.caesar.decorator.jenkins.JenkinsParameterDecorator;
import com.baiyi.caesar.decorator.jenkins.JobDeploymentDecorator;
import com.baiyi.caesar.decorator.jenkins.JobTplDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.vo.application.CdJobVO;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.service.jenkins.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
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

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private JobTplDecorator jobTplDecorator;

    @Resource
    private JenkinsParameterDecorator jenkinsParameterDecorator;

    @Resource
    private BuildViewDecorator buildViewDecorator;

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

    public void decorator(CdJobVO.IDeploymentJob iDeploymentJob) {
        if (IDUtil.isEmpty(iDeploymentJob.getDeploymentJobId())) return;
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(iDeploymentJob.getDeploymentJobId());
        if (csCdJob != null) {
            iDeploymentJob.setCdJob(BeanCopierUtil.copyProperties(csCdJob, CdJobVO.CdJob.class));
        }
    }

    public CdJobVO.CdJob decorator(CsCdJob csCdJob) {
        return decorator(BeanCopierUtil.copyProperties(csCdJob, CdJobVO.CdJob.class));
    }

    public CdJobVO.CdJob decorator(CdJobVO.CdJob cdJob) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(cdJob.getCiJobId());
        cdJob.setEnvType(csCiJob.getEnvType());
        // 装饰 环境信息
        envDecorator.decorator(cdJob);
        jobTplDecorator.decorator(cdJob);  // 任务模版
        jenkinsParameterDecorator.decorator(cdJob);   // 参数
        buildViewDecorator.decorator(cdJob);    // buildViews

        return cdJob;
    }


}
