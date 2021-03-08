package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.common.util.TimeAgoUtil;
import com.baiyi.caesar.common.util.TimeUtil;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.decorator.jenkins.context.JobBuildContext;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.*;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.jenkins.*;
import com.baiyi.caesar.service.server.OcServerService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/28 10:18 上午
 * @Version 1.0
 */
@Component
public class JobDeploymentDecorator {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private JobBuildArtifactDecorator jobBuildArtifactDecorator;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsOssBucketService ossBucketService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private CsJobBuildServerService csJobBuildServerService;

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
        if (!CollectionUtils.isEmpty(jobBuilds)) {
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
        } else {
            return JobBuildContext.builder().build();
        }
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
        // Ago
        jobBuild.setAgo(acqAgo(jobBuild));
        // User
        jobBuild.setUser(acqUser(jobBuild));

        if (extend == 0) return jobBuild;

        // 组装工作引擎
        jobBuild.setJobEngine(acqJobEngine(jobBuild, context));
        jobBuild.setJobBuildUrl(acqJobBuildUrl(jobBuild, context));

        jobBuild.setArtifacts(acqArtifacts(jobBuild, context));
        // BuildTimes
        jobBuild.setBuildTime(acqBuildTimes(jobBuild));
        // Executors
        jobBuild.setExecutors(getExecutorsByBuildId(jobBuild.getId()));
        // Servers
        jobBuild.setServers(acqServers(jobBuild));

        return jobBuild;
    }

    /**
     * 获取工作引擎
     *
     * @param jobBuild
     * @param context
     * @return
     */
    private JobEngineVO.JobEngine acqJobEngine(CdJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        return context.getJobEngineMap().get(jobBuild.getJobEngineId());
    }

    /**
     * 装饰工作引擎
     *
     * @param jobBuild
     * @param context
     */
    private String acqJobBuildUrl(CdJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        JobEngineVO.JobEngine jobEngine = context.getJobEngineMap().get(jobBuild.getJobEngineId());
        return Joiner.on("/").join(jobEngine.getJobUrl(), jobBuild.getEngineBuildNumber());
    }

    private List<BuildArtifactVO.BuildArtifact> acqArtifacts(CdJobBuildVO.JobBuild jobBuild, JobBuildContext context) {
        List<CsJobBuildArtifact> artifacts = csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(BuildType.DEPLOYMENT.getType(), jobBuild.getId());
        if (!CollectionUtils.isEmpty(artifacts)) {
            return jobBuildArtifactDecorator.decorator(artifacts, context.getCsOssBucket());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private List<DeploymentServerVO.BuildServer> acqServers(CdJobBuildVO.JobBuild jobBuild) {
        List<CsJobBuildServer> csJobBuildServers = csJobBuildServerService.queryCsJobBuildServerByBuildId(BuildType.DEPLOYMENT.getType(), jobBuild.getId());
        return BeanCopierUtil.copyListProperties(csJobBuildServers, DeploymentServerVO.BuildServer.class);
    }


    private String acqBuildTimes(CdJobBuildVO.JobBuild jobBuild) {
        if (jobBuild.getStartTime() != null && jobBuild.getEndTime() != null) {
            long buildTime = jobBuild.getEndTime().getTime() - jobBuild.getStartTime().getTime();
            return TimeUtil.acqBuildTime(buildTime);
        }
        return "";
    }

    private String acqAgo(CdJobBuildVO.JobBuild jobBuild) {
        return TimeAgoUtil.format(jobBuild.getStartTime());
    }

    private UserVO.User acqUser(CdJobBuildVO.JobBuild jobBuild) {
        if (!StringUtils.isEmpty(jobBuild.getUsername())) {
            OcUser ocUser = ocUserService.queryOcUserByUsername(jobBuild.getUsername());
            if (ocUser != null) {
                ocUser.setPassword("");
                return BeanCopierUtil.copyProperties(ocUser, UserVO.User.class);
            }
        }
        return new UserVO.User();
    }

    public List<BuildExecutorVO.BuildExecutor> getExecutorsByBuildId(int buildId) {
        List<CsJobBuildExecutor> executors = csJobBuildExecutorService.queryCsJobBuildExecutorByBuildId(BuildType.DEPLOYMENT.getType(), buildId);
        return executors.stream().map(e -> {
                    BuildExecutorVO.BuildExecutor buildExecutor = BeanCopierUtil.copyProperties(e, BuildExecutorVO.BuildExecutor.class);
                    OcServer ocServer = ocServerService.queryOcServerByIp(buildExecutor.getPrivateIp());
                    if (ocServer != null)
                        buildExecutor.setServer(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
                    return buildExecutor;
                }
        ).collect(Collectors.toList());
    }
}
