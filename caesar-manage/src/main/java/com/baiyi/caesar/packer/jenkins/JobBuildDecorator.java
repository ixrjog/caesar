package com.baiyi.caesar.packer.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.packer.application.JobEnginePacker;
import com.baiyi.caesar.packer.jenkins.base.BaseJenkinsDecorator;
import com.baiyi.caesar.packer.jenkins.context.JobBuildContext;
import com.baiyi.caesar.packer.jenkins.util.JenkinsUtil;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.packer.base.BaseDecorator.NOT_EXTEND;

/**
 * @Author baiyi
 * @Date 2021/1/14 10:21 上午
 * @Version 1.0
 */
@Component
public class JobBuildDecorator extends BaseJenkinsDecorator {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEnginePacker jobEngineDecorator;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsOssBucketService ossBucketService;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private JobTplDecorator jobTplDecorator;

    public List<CiJobBuildVO.JobBuild> decorator(List<CsCiJobBuild> jobBuilds, Integer extend) {
        JobBuildContext context = buildJobBuildContext(jobBuilds);
        return jobBuilds.stream().map(e ->
                decorator(e, context, extend)
        ).collect(Collectors.toList());
    }

    public CiJobBuildVO.JobBuild decorator(CsCiJobBuild jobBuild, Integer extend) {
        JobBuildContext context = buildJobBuildContext(Lists.newArrayList(jobBuild));
        return decorator(jobBuild, context, extend);
    }

    private JobBuildContext buildJobBuildContext(List<CsCiJobBuild> jobBuilds) {
        if (CollectionUtils.isEmpty(jobBuilds)) return JobBuildContext.builder().build();

        CsCiJobBuild jobBuild = jobBuilds.get(0);
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(jobBuild.getCiJobId());
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(csCiJob.getScmMemberId());
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(csApplicationScmMember.getScmId());
        JobTplVO.JobTpl jobTpl = new JobTplVO.JobTpl();
        if (!IDUtil.isEmpty(csCiJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(csCiJob.getJobTplId());
            jobTpl = jobTplDecorator.decorator(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class), NOT_EXTEND);
        }
        return JobBuildContext.builder()
                .csCiJob(csCiJob)
                .csOssBucket(ossBucketService.queryCsOssBucketById(csCiJob.getOssBucketId()))
                .jobEngineMap(acqJobEngineMap(jobBuilds))
                .csGitlabProject(csGitlabProject)
                .jobTpl(jobTpl)
                .build();

    }

    private Map<Integer, JobEngineVO.JobEngine> acqJobEngineMap(List<CsCiJobBuild> jobBuilds) {
        Map<Integer, JobEngineVO.JobEngine> jobEngineMap = Maps.newHashMap();
        jobBuilds.parallelStream().forEach(jobBuild -> {
            if (!jobEngineMap.containsKey(jobBuild.getJobEngineId())) {
                CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobBuild.getJobEngineId());
                if (csJobEngine != null) {
                    JobEngineVO.JobEngine jobEngine = jobEngineDecorator.wrap(BeanCopierUtil.copyProperties(csJobEngine, JobEngineVO.JobEngine.class));
                    jobEngineMap.put(jobBuild.getJobEngineId(), jobEngine);
                }
            }
        });
        return jobEngineMap;
    }


    public CiJobBuildVO.JobBuild decorator(CsCiJobBuild csCiJobBuild, JobBuildContext context, Integer extend) {
        CiJobBuildVO.JobBuild jobBuild = BeanCopierUtil.copyProperties(csCiJobBuild, CiJobBuildVO.JobBuild.class);

        decoratorJobBuild(jobBuild, extend);

        if (NOT_EXTEND == extend) return jobBuild;

        decoratorBuildArtifacts(jobBuild, context);  // 装饰 构件
        decoratorJobEngine(jobBuild, context);    // 装饰 工作引擎
        decoratorJobDetailUrl(jobBuild); // 装饰 任务详情Url
        decoratorBuildChanges(jobBuild, context);  // 装饰 变更记录
        decoratorBuildExecutors(jobBuild); // 装饰 执行器
        jobBuild.setCommitDetails(JenkinsUtil.buildCommitDetails(jobBuild, context));  // 组装Commit详情
        jobBuild.setSupportRollback(JenkinsUtil.buildSupportRollback(context));  // 支持回滚

        return jobBuild;
    }


}
