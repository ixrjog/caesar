package com.baiyi.caesar.factory.jenkins.engine.impl;

import com.aliyun.oss.model.OSSObjectSummary;
import com.baiyi.caesar.aliyun.oss.handler.AliyunOSSHandler;
import com.baiyi.caesar.builder.jenkins.JobBuildChangeBuilder;
import com.baiyi.caesar.builder.jenkins.JobBuildArtifactBuilder;
import com.baiyi.caesar.builder.jenkins.JobBuildExecutorBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.RedisKeyUtils;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.facade.ServerBaseFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsJobFacade;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.factory.jenkins.engine.JobEngineHandler;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.handler.JenkinsJobHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.jenkins.*;
import com.baiyi.caesar.service.server.OcServerService;
import com.baiyi.caesar.util.JobBuildUtils;
import com.offbytwo.jenkins.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/8/6 10:56 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class JobEngineHandlerImpl implements JobEngineHandler {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JenkinsJobFacade jenkinsCiJobFacade;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JobEngineDecorator ciJobEngineDecorator;

    @Resource
    private JenkinsJobHandler jenkinsJobHandler;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private AliyunOSSHandler aliyunOSSHandler;

    @Resource
    private CsOssBucketService csOssBucketService;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private RedisUtil redisUtil;

    private static final int TRACK_SLEEP_SECONDS = 5;

    @Override
    public BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCiJob csJob) {
        return acqJobEngineByJobEngines(queryJobEngine(BuildType.BUILD.getType(), csJob.getId()));
    }

    @Override
    public BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCdJob csJob) {
        return acqJobEngineByJobEngines(queryJobEngine(BuildType.DEPLOYMENT.getType(), csJob.getId()));
    }

    private BusinessWrapper<JobEngineVO.JobEngine> acqJobEngineByJobEngines(List<CsJobEngine> csJobEngines) {
        if (CollectionUtils.isEmpty(csJobEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_ENGINE_NOT_CONFIGURED); // 工作引擎未配置
        List<CsJobEngine> activeEngines = csJobEngines.stream().filter(e ->
                tryJenkinsInstanceActive(e.getJenkinsInstanceId())
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(activeEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_NO_ENGINES_AVAILABLE); // 没有可用的工作引擎
        return new BusinessWrapper<>(buildRandomCiJobEngine(activeEngines));
    }

    @Override
    public JobEngineVO.JobEngine acqJobEngineByJobEngineId(int jobEngineId) {
        CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobEngineId);
        return ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csJobEngine, JobEngineVO.JobEngine.class));
    }

    private JobEngineVO.JobEngine buildRandomCiJobEngine(List<CsJobEngine> activeEngines) {
        Random random = new Random();
        int n = random.nextInt(activeEngines.size());
        CsJobEngine csCiJobEngine = activeEngines.get(n);
        return ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobEngine, JobEngineVO.JobEngine.class));
    }

    private boolean tryJenkinsInstanceActive(int jenkinsInstanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jenkinsInstanceId);
        if (csJenkinsInstance == null) return false;
        return csJenkinsInstance.getIsActive();
    }

    private List<CsJobEngine> queryJobEngine(int buildType, int ciJobId) {
        List<CsJobEngine> csJobEngines = csJobEngineService.queryCsJobEngineByJobId(buildType, ciJobId);
        if (CollectionUtils.isEmpty(csJobEngines))
            jenkinsCiJobFacade.createJobEngine(buildType, ciJobId);
        return csJobEngineService.queryCsJobEngineByJobId(buildType, ciJobId);
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void trackJobBuild(BuildJobContext context) {
        while (true) {
            try {
                trackJobBuildHeartbeat(BuildType.BUILD.getType(), context.getJobBuild().getId()); // 心跳
                JobWithDetails job = jenkinsServerHandler.getJob(context.getJobEngine().getJenkinsInstance().getName(), context.getJobBuild().getJobName());
                Build build = jenkinsJobHandler.getJobBuildByNumber(job, context.getJobBuild().getEngineBuildNumber());
                BuildWithDetails buildWithDetails = build.details();
                recordJobEngine(job, context.getJobEngine());
                if (buildWithDetails.isBuilding()) {
                    trackJobBuildComputer(context);
                    TimeUnit.SECONDS.sleep(TRACK_SLEEP_SECONDS); // 执行中
                } else {
                    // 任务完成
                    context.setBuildWithDetails(buildWithDetails);
                    recordJobBuild(context);
                    buildEndNotify(context);
                    break;
                }
            } catch (RetryException e) {
                log.error("重试获取JobBuild失败，jobName = {}, buildNumber ={}", context.getCsCiJob().getName(), context.getJobBuild().getEngineBuildNumber());
                break;
            } catch (InterruptedException | IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void buildEndNotify(BuildJobContext context) {
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(context.getCsCiJob().getJobType());
        dingtalkNotify.doNotify(NoticePhase.END.getType(), context);
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void trackJobBuild(DeploymentJobContext context) {
        while (true) {
            try {
                trackJobBuildHeartbeat(BuildType.DEPLOYMENT.getType(), context.getJobBuild().getId()); // 心跳
                JobWithDetails job = jenkinsServerHandler.getJob(context.getJobEngine().getJenkinsInstance().getName(), context.getJobBuild().getJobName());
                Build build = jenkinsJobHandler.getJobBuildByNumber(job, context.getJobBuild().getEngineBuildNumber());
                BuildWithDetails buildWithDetails = build.details();
                recordJobEngine(job, context.getJobEngine());
                if (buildWithDetails.isBuilding()) {
                    trackJobBuildComputer(context);
                    TimeUnit.SECONDS.sleep(TRACK_SLEEP_SECONDS); // 执行中
                } else {
                    // 任务完成
                    context.setBuildWithDetails(buildWithDetails);
                    recordJobBuild(context);
                    break;
                }
            } catch (RetryException e) {
                log.error("重试获取JobDeployment失败，jobName = {}, buildNumber ={}", context.getCsCiJob().getName(), context.getJobBuild().getEngineBuildNumber());
                break;
            } catch (InterruptedException | IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void trackJobBuildComputer(BuildJobContext context) {
        Map<String, Computer> computerMap = jenkinsServerHandler.getComputerMap(context.getJobEngine().getJenkinsInstance().getName());
        computerMap.keySet().forEach(k -> {
            if (!k.equals("master")) {
                Computer computer = computerMap.get(k);
                try {
                    ComputerWithDetails computerWithDetails = computer.details();
                    computerWithDetails.getExecutors().forEach(executor -> {
                        if (executor.getCurrentExecutable() != null) {
                            Job job = executor.getCurrentExecutable();
                            JobBuild jobBuild = JobBuildUtils.convert(job.getUrl());
                            if (jobBuild.isJobBuild(context))
                                recordJobBuildComputer(context, computerWithDetails, jobBuild);
                        }
                    });
                } catch (IOException ignored) {
                }
            }
        });
    }

    private void trackJobBuildComputer(DeploymentJobContext context) {
        Map<String, Computer> computerMap = jenkinsServerHandler.getComputerMap(context.getJobEngine().getJenkinsInstance().getName());
        computerMap.keySet().forEach(k -> {
            if (!k.equals("master")) {
                Computer computer = computerMap.get(k);
                try {
                    ComputerWithDetails computerWithDetails = computer.details();
                    computerWithDetails.getExecutors().forEach(executor -> {
                        if (executor.getCurrentExecutable() != null) {
                            Job job = executor.getCurrentExecutable();
                            JobBuild jobBuild = JobBuildUtils.convert(job.getUrl());
                            if (jobBuild.isJobBuild(context))
                                recordJobBuildComputer(context, computerWithDetails, jobBuild);
                        }
                    });
                } catch (IOException ignored) {
                }
            }
        });
    }

    private void recordJobBuildComputer(BuildJobContext jobBuildContext, ComputerWithDetails computerWithDetails, JobBuild jobBuild) {
        CsJobBuildExecutor pre = JobBuildExecutorBuilder.build(jobBuildContext, computerWithDetails, jobBuild);
        if (csJobBuildExecutorService.queryCsJobBuildExecutorByUniqueKey(BuildType.BUILD.getType(), pre.getBuildId(), pre.getNodeName()) != null)
            return;
        recordJobBuildComputer(jobBuildContext.getJobEngine(), pre);
    }

    private void recordJobBuildComputer(DeploymentJobContext context, ComputerWithDetails computerWithDetails, JobBuild jobBuild) {
        CsJobBuildExecutor pre = JobBuildExecutorBuilder.build(context, computerWithDetails, jobBuild);
        if (csJobBuildExecutorService.queryCsJobBuildExecutorByUniqueKey(BuildType.DEPLOYMENT.getType(), pre.getBuildId(), pre.getNodeName()) != null)
            return;
        recordJobBuildComputer(context.getJobEngine(), pre);
    }

    private void recordJobBuildComputer(JobEngineVO.JobEngine jobEngine, CsJobBuildExecutor pre) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jobEngine.getJenkinsInstanceId());
        List<OcServer> nodeList = ocServerService.queryOcServerByServerGroupId(csJenkinsInstance.getNodeServerGroupId());
        for (OcServer ocServer : nodeList) {
            String nodeName = ServerBaseFacade.acqServerName(ocServer);
            if (nodeName.equals(pre.getNodeName())) {
                pre.setPrivateIp(ocServer.getPrivateIp());
                break;
            }
        }
        csJobBuildExecutorService.addCsJobBuildExecutor(pre);
    }

    private void recordJobEngine(JobWithDetails job, JobEngineVO.JobEngine jobEngine) {
        if (job.getNextBuildNumber() == jobEngine.getNextBuildNumber()) return;
        jobEngine.setNextBuildNumber(job.getNextBuildNumber());
        jobEngine.setLastBuildNumber(job.getNextBuildNumber() - 1);
        csJobEngineService.updateCsJobEngine(BeanCopierUtils.copyProperties(jobEngine, CsJobEngine.class));
    }

    /**
     * 记录构建信息
     */
    private void recordJobBuild(BuildJobContext context) {
        CiJobBuildVO.JobBuild jobBuild = context.getJobBuild();
        jobBuild.setBuildStatus(context.getBuildWithDetails().getResult().name());
        jobBuild.setEndTime(new Date());
        jobBuild.setFinalized(true);
        csCiJobBuildService.updateCsCiJobBuild(BeanCopierUtils.copyProperties(jobBuild, CsCiJobBuild.class));
        context.setJobBuild(jobBuild);
        context.getBuildWithDetails().getArtifacts().forEach(e -> saveJobBuildArtifact(context, e));
        recordJobBuildChanges(context);
    }

    private void recordJobBuild(DeploymentJobContext context) {
        CdJobBuildVO.JobBuild jobBuild = context.getJobBuild();
        jobBuild.setBuildStatus(context.getBuildWithDetails().getResult().name());
        jobBuild.setEndTime(new Date());
        jobBuild.setFinalized(true);
        csCdJobBuildService.updateCsCdJobBuild(BeanCopierUtils.copyProperties(jobBuild, CsCdJobBuild.class));
        context.setJobBuild(jobBuild);
        context.getBuildWithDetails().getArtifacts().forEach(e -> saveJobBuildArtifact(context, e));
        // recordJobBuildChanges(context);
    }

    private void saveJobBuildArtifact(BuildJobContext context, Artifact artifact) {
        CsJobBuildArtifact pre = buildJobBuildArtifact(context, artifact);
        if (csJobBuildArtifactService.queryCsJobBuildArtifactByUniqueKey(context.getBuildType(), pre.getBuildId(), pre.getArtifactFileName()) == null)
            csJobBuildArtifactService.addCsJobBuildArtifact(pre);
    }

    private void saveJobBuildArtifact(DeploymentJobContext context, Artifact artifact) {
        CsJobBuildArtifact pre = buildJobBuildArtifact(context, artifact);
        if (csJobBuildArtifactService.queryCsJobBuildArtifactByUniqueKey(context.getBuildType(), pre.getBuildId(), pre.getArtifactFileName()) == null)
            csJobBuildArtifactService.addCsJobBuildArtifact(pre);
    }

    /**
     * 变更记录
     *
     * @param jobBuildContext
     */
    private void recordJobBuildChanges(BuildJobContext jobBuildContext) {
        List<BuildChangeSet> buildChanges = jobBuildContext.getBuildWithDetails().getChangeSets();
        buildChanges.forEach(set -> set.getItems().forEach(e -> saveJobBuildChange(jobBuildContext, e)));
    }

    private void saveJobBuildChange(BuildJobContext jobBuildContext, BuildChangeSetItem buildChangeSetItem) {
        CsJobBuildChange pre = JobBuildChangeBuilder.build(jobBuildContext, buildChangeSetItem);
        if (csJobBuildChangeService.queryCsJobBuildChangeByUniqueKey(BuildType.BUILD.getType(), pre.getJobId(), pre.getCommitId()) == null)
            csJobBuildChangeService.addCsJobBuildChange(pre);
    }

    private CsJobBuildArtifact buildJobBuildArtifact(BuildJobContext context, Artifact artifact) {
        CsJobBuildArtifact csJobBuildArtifact = JobBuildArtifactBuilder.build(context, artifact);
        CsOssBucket ossBucket = acqOssBucket(context.getCsCiJob());
        IBuildJobHandler iBuildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(context.getCsCiJob().getJobType());
        String ossPath = iBuildJobHandler.acqOssPath(context.getJobBuild(), csJobBuildArtifact);
        csJobBuildArtifact.setStoragePath(ossPath);

        List<OSSObjectSummary> objects = aliyunOSSHandler.listObjects(ossBucket.getName(), ossPath);
        if (!CollectionUtils.isEmpty(objects)) {
            OSSObjectSummary ossObjectSummary = objects.get(0);
            csJobBuildArtifact.setStoragePath(ossObjectSummary.getKey());
            csJobBuildArtifact.setArtifactSize(ossObjectSummary.getSize());
        }
        return csJobBuildArtifact;
    }

    private CsJobBuildArtifact buildJobBuildArtifact(DeploymentJobContext context, Artifact artifact) {
        CsJobBuildArtifact csJobBuildArtifact = JobBuildArtifactBuilder.build(context, artifact);
        CsOssBucket ossBucket = acqOssBucket(context.getCsCiJob());
        IDeploymentJobHandler iDeploymentJobHandler = DeploymentJobHandlerFactory.getDeploymentJobByKey(context.getCsCdJob().getJobType());
        String ossPath = iDeploymentJobHandler.acqOssPath(context.getJobBuild(), csJobBuildArtifact);
        csJobBuildArtifact.setStoragePath(ossPath);

        List<OSSObjectSummary> objects = aliyunOSSHandler.listObjects(ossBucket.getName(), ossPath);
        if (!CollectionUtils.isEmpty(objects)) {
            OSSObjectSummary ossObjectSummary = objects.get(0);
            csJobBuildArtifact.setStoragePath(ossObjectSummary.getKey());
            csJobBuildArtifact.setArtifactSize(ossObjectSummary.getSize());
        }
        return csJobBuildArtifact;
    }

    private CsOssBucket acqOssBucket(CsCiJob csCiJob) {
        return csOssBucketService.queryCsOssBucketById(csCiJob.getOssBucketId());
    }

    @Override
    public void trackJobBuildHeartbeat(int buildType, int buildId) {
        if (buildType == BuildType.BUILD.getType())
            redisUtil.set(RedisKeyUtils.getJobBuildKey(buildId), true, 30);
        if (buildType == BuildType.DEPLOYMENT.getType())
            redisUtil.set(RedisKeyUtils.getJobDeploymentKey(buildId), true, 30);
    }


}