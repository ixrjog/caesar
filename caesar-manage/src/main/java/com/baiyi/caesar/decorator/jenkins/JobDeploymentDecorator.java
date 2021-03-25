package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.decorator.jenkins.base.BaseJenkinsDecorator;
import com.baiyi.caesar.decorator.jenkins.context.JobBuildContext;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
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

/**
 * @Author baiyi
 * @Date 2020/8/28 10:18 上午
 * @Version 1.0
 */
@Component
public class JobDeploymentDecorator extends BaseJenkinsDecorator {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsOssBucketService ossBucketService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private JobTplDecorator jobTplDecorator;

    public CdJobBuildVO.JobBuild decorator(CsCdJobBuild jobBuild, Integer extend) {
        JobBuildContext context = buildJobBuildContext(Lists.newArrayList(jobBuild));
        return decorator(jobBuild, context, extend);
    }

    public List<CdJobBuildVO.JobBuild> decorator(List<CsCdJobBuild> jobBuilds, Integer extend) {
        JobBuildContext context = buildJobBuildContext(jobBuilds);
        return jobBuilds.stream().map(e ->
                decorator(e, context, extend)
        ).collect(Collectors.toList());
    }

    private JobBuildContext buildJobBuildContext(List<CsCdJobBuild> jobBuilds) {
        if (CollectionUtils.isEmpty(jobBuilds)) return JobBuildContext.builder().build();
        CsCdJobBuild jobBuild = jobBuilds.get(0);
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(jobBuild.getCdJobId());
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(csCdJob.getCiJobId());

        JobTplVO.JobTpl jobTpl = new JobTplVO.JobTpl();
        if (!IDUtil.isEmpty(csCiJob.getJobTplId())) {
            CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(csCiJob.getJobTplId());
            jobTpl = jobTplDecorator.decorator(BeanCopierUtil.copyProperties(csJobTpl, JobTplVO.JobTpl.class), 0);
        }
        return JobBuildContext.builder()
                .csCiJob(csCiJob)
                .csOssBucket(ossBucketService.queryCsOssBucketById(csCiJob.getOssBucketId()))
                .jobTpl(jobTpl)
                .jobEngineMap(acqJobEngineMap(jobBuilds))
                .build();
    }

    private Map<Integer, JobEngineVO.JobEngine> acqJobEngineMap(List<CsCdJobBuild> jobBuilds) {
        Map<Integer, JobEngineVO.JobEngine> jobEngineMap = Maps.newHashMap();
        jobBuilds.parallelStream().forEach(jobBuild -> {
            if (!jobEngineMap.containsKey(jobBuild.getJobEngineId())) {
                CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobBuild.getJobEngineId());
                if (csJobEngine != null) {
                    JobEngineVO.JobEngine jobEngine = jobEngineDecorator.decorator(BeanCopierUtil.copyProperties(csJobEngine, JobEngineVO.JobEngine.class));
                    jobEngineMap.put(jobBuild.getJobEngineId(), jobEngine);
                }
            }
        });
        return jobEngineMap;
    }

    private CdJobBuildVO.JobBuild decorator(CsCdJobBuild csCdJobBuild, JobBuildContext context, Integer extend) {
        CdJobBuildVO.JobBuild jobBuild = BeanCopierUtil.copyProperties(csCdJobBuild, CdJobBuildVO.JobBuild.class);

        decoratorJobBuild(jobBuild, extend);

        if (extend == 0) return jobBuild;
        decoratorBuildArtifacts(jobBuild,context);  // 组装构件
        decoratorJobEngine(jobBuild, context);    // 组装工作引擎
        decoratorJobDetailUrl(jobBuild); // 装饰 任务详情Url
        decoratorBuildExecutors(jobBuild); // 装饰 执行器
        decoratorDeploymentServers(jobBuild);  // 装饰 部署服务器

        return jobBuild;
    }

}
