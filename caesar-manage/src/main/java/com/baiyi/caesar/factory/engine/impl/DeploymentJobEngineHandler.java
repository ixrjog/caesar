package com.baiyi.caesar.factory.engine.impl;

import com.baiyi.caesar.builder.jenkins.JobBuildArtifactBuilder;
import com.baiyi.caesar.builder.jenkins.JobBuildExecutorBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.RedisKeyUtils;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildExecutor;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.context.BaseJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsJobBuildExecutorService;
import com.baiyi.caesar.util.JobBuildUtils;
import com.offbytwo.jenkins.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/11/20 9:56 上午
 * @Version 1.0
 */
@Slf4j
@Component("DeploymentJobEngineHandler")
public class DeploymentJobEngineHandler<T extends BaseJobContext> extends BaseJobEngineHandler<T> {

    @Override
    public Integer getKey() {
        return BuildType.DEPLOYMENT.getType();
    }

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void trackJobBuild(T buildJobContext) {
        DeploymentJobContext context = (DeploymentJobContext) buildJobContext;
        while (true) {
            try {
                // 心跳
                trackJobBuildHeartbeat(context.getJobBuild().getId());
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
                    buildEndNotify(context); // 任务结束通知
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

    @Override
    protected CsJobBuildArtifact acqJobBuildArtifact(T context, Artifact artifact) {
        DeploymentJobContext deploymentJobContext = (DeploymentJobContext) context;
        return JobBuildArtifactBuilder.build(deploymentJobContext, artifact);
    }

    @Override
    protected CsOssBucket acqOssBucket(T context) {
        DeploymentJobContext deploymentJobContext = (DeploymentJobContext) context;
        return csOssBucketService.queryCsOssBucketById(deploymentJobContext.getCsCiJob().getOssBucketId());
    }

    @Override
    protected String acqOssPath(T context, CsJobBuildArtifact csJobBuildArtifact) {
        DeploymentJobContext deploymentJobContext = (DeploymentJobContext) context;
        IDeploymentJobHandler iDeploymentJobHandler = DeploymentJobHandlerFactory.getDeploymentJobByKey(deploymentJobContext.getCsCdJob().getJobType());
        return iDeploymentJobHandler.acqOssPath(deploymentJobContext.getJobBuild(), csJobBuildArtifact);
    }

    private void recordJobBuildComputer(DeploymentJobContext context, ComputerWithDetails computerWithDetails, JobBuild jobBuild) {
        CsJobBuildExecutor pre = JobBuildExecutorBuilder.build(context, computerWithDetails, jobBuild);
        if (csJobBuildExecutorService.queryCsJobBuildExecutorByUniqueKey(getKey(), pre.getBuildId(), pre.getNodeName()) != null)
            return;
        recordJobBuildComputer(context.getJobEngine(), pre);
    }

    private void recordJobBuild(DeploymentJobContext context) {
        CdJobBuildVO.JobBuild jobBuild = context.getJobBuild();
        jobBuild.setBuildStatus(context.getBuildWithDetails().getResult().name());
        jobBuild.setEndTime(new Date());
        jobBuild.setFinalized(true);
        csCdJobBuildService.updateCsCdJobBuild(BeanCopierUtils.copyProperties(jobBuild, CsCdJobBuild.class));
        context.setJobBuild(jobBuild);
        context.getBuildWithDetails().getArtifacts().forEach(e -> saveJobBuildArtifact((T) context, e));
    }


    private void buildEndNotify(DeploymentJobContext context) {
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(context.getCsCdJob().getJobType());
        dingtalkNotify.doNotify(NoticePhase.END.getType(), context);
    }


    @Override
    public void trackJobBuildHeartbeat(int buildId) {
        redisUtil.set(RedisKeyUtils.getJobDeploymentKey(buildId), true, 10);
    }

}
