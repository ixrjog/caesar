package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.bo.SonarParamsBO;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.common.util.JenkinsUtil;
import com.baiyi.caesar.decorator.application.base.BaseJobDecorator;
import com.baiyi.caesar.decorator.env.EnvDecorator;
import com.baiyi.caesar.decorator.jenkins.JobBuildsDecorator;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.sonar.SonarEntry;
import com.baiyi.caesar.domain.vo.sonar.SonarMeasures;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import com.baiyi.caesar.sonar.SonarQubeServer;
import com.baiyi.caesar.sonar.config.SonarConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/29 2:08 下午
 * @Version 1.0
 */
@Component
public class CiJobDecorator extends BaseJobDecorator {

    @Resource
    private EnvDecorator envDecorator;

    @Resource
    private CsDingtalkService csDingtalkService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    private TagDecorator tagDecorator;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private JobBuildsDecorator jobBuildsDecorator;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    @Resource
    private CdJobDecorator cdJobDecorator;

    @Resource
    private SonarQubeServer sonarQubeServer;

    @Resource
    private SonarConfig sonarConfig;

    public CiJobVO.CiJob decorator(CiJobVO.CiJob ciJob, CsJobTpl csJobTpl) {
        List<CsJobEngine> csCiJobEngines = csJobEngineService.queryCsJobEngineByJobId(BuildType.BUILD.getType(), ciJob.getId());
        AtomicReference<Boolean> needUpgrade = new AtomicReference<>(false);
        if (!CollectionUtils.isEmpty(csCiJobEngines)) {
            ciJob.setJobEngines(
                    csCiJobEngines.stream().map(e -> {
                        JobEngineVO.JobEngine jobEngine = BeanCopierUtil.copyProperties(e, JobEngineVO.JobEngine.class);
                        jobEngine.setNeedUpgrade(csJobTpl.getTplVersion() > jobEngine.getTplVersion());
                        if (jobEngine.getNeedUpgrade())
                            needUpgrade.set(true);
                        return jobEngineDecorator.decorator(jobEngine);
                    }).collect(Collectors.toList())
            );
        }
        ciJob.setNeedUpgrade(needUpgrade.get());
        return ciJob;
    }

    public CiJobVO.CiJob decorator(CiJobVO.CiJob ciJob, Integer extend) {
        if (extend == 0) return ciJob;
        // 装饰 环境信息
        envDecorator.decorator(ciJob);

        // 钉钉
        if (!IDUtil.isEmpty(ciJob.getDingtalkId())) {
            CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(ciJob.getDingtalkId());
            if (csDingtalk != null)
                ciJob.setDingtalk(BeanCopierUtil.copyProperties(csDingtalk, DingtalkVO.Dingtalk.class));
        }
        // 任务模版
        if (!IDUtil.isEmpty(ciJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(ciJob.getJobTplId());
            if (csJobTpl != null)
                ciJob.setJobTpl(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class));
        }
        // SCM
        if (!IDUtil.isEmpty(ciJob.getScmMemberId())) {
            CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(ciJob.getScmMemberId());
            if (csApplicationScmMember != null) {
                ciJob.setScmMember(BeanCopierUtil.copyProperties(csApplicationScmMember, ApplicationVO.ScmMember.class));
                ciJob.setBusinessType(BusinessType.GITLAB_PROJECT.getType());
                tagDecorator.decorator(ciJob);
                // ciJob.setTags(tagDecorator.acqTags(BusinessType.GITLAB_PROJECT.getType(), csApplicationScmMember.getScmId()));
            }
        }
        // Bucket
        if (!IDUtil.isEmpty(ciJob.getOssBucketId())) {
            CsOssBucket csOssBucket = csOssBucketService.queryCsOssBucketById(ciJob.getOssBucketId());
            if (csOssBucket != null) {
                ciJob.setBucket(BeanCopierUtil.copyProperties(csOssBucket, OssBucketVO.Bucket.class));
            }
        }

        // 参数
        JenkinsJobParameters jenkinsJobParameters = JenkinsUtil.convert(ciJob.getParameterYaml());
        Map<String, String> params = JenkinsUtil.convert(jenkinsJobParameters);
        ciJob.setParameters(params);

        JobBuildParam.BuildPageQuery query = new JobBuildParam.BuildPageQuery();
        query.setCiJobId(ciJob.getId());
        query.setExtend(1);
        ciJob.setBuildViews(acqCiJobBuildView(query));

        // cdJob
        if (!IDUtil.isEmpty(ciJob.getDeploymentJobId())) {
            CsCdJob csCdJob = csCdJobService.queryCsCdJobById(ciJob.getDeploymentJobId());
            if (csCdJob != null) {
                ciJob.setCdJob(cdJobDecorator.decorator(csCdJob));
            }
        }

        // SonarQube
        if (ciJob.getEnableSonar()) {
            CsApplication csApplication = csApplicationService.queryCsApplicationById(ciJob.getApplicationId());
            String projectKey = Joiner.on("_").join(csApplication.getApplicationKey(), ciJob.getJobKey());
            ciJob.setSonarQube(buildSonarQube(projectKey));
        }
        return ciJob;
    }

    private CiJobVO.SonarQube buildSonarQube(String projectKey) {
        SonarMeasures sonarMeasures = sonarQubeServer.queryMeasuresComponent(projectKey);
        CiJobVO.SonarQube sonarQube = new CiJobVO.SonarQube();
        sonarQube.setMeasures(convertMeasuresMap(sonarMeasures));
        sonarQube.setAlertStatus(new SonarParamsBO(projectKey, "alert_status").toString());
        // http://sonar.xinc818.com/dashboard?id=DATA-CENTER_data-center-server-dev
        sonarQube.setProjectUrl(Joiner.on("/").join(sonarConfig.getUrl(), "dashboard?id=") + projectKey);
        return sonarQube;
    }

    private Map<String, SonarEntry.Measure> convertMeasuresMap(SonarMeasures sonarMeasures) {
        if (sonarMeasures.getComponent() == null || CollectionUtils.isEmpty(sonarMeasures.getComponent().getMeasures()))
            return Maps.newHashMap();
        return sonarMeasures.getComponent().getMeasures().stream().collect(Collectors.toMap(SonarEntry.Measure::getMetric, a -> a, (k1, k2) -> k1));
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

            assembleJobBuildView(jobBuildView,e.getFinalized() ,e.getBuildStatus());

            jobBuildView.setExecutors(jobBuildsDecorator.getBuildExecutorByBuildId(e.getId()));
            return jobBuildView;
        }).collect(Collectors.toList());
    }
}
