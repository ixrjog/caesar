package com.baiyi.caesar.factory.engine.impl;

import com.baiyi.caesar.builder.jenkins.JobBuildArtifactBuilder;
import com.baiyi.caesar.builder.jenkins.JobBuildChangeBuilder;
import com.baiyi.caesar.builder.jenkins.JobBuildExecutorBuilder;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.RedisKeyUtils;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.context.BaseJobContext;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsJobBuildExecutorService;
import com.baiyi.caesar.util.JobBuildUtils;
import com.offbytwo.jenkins.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2020/11/20 9:54 上午
 * @Version 1.0
 */
@Slf4j
@Component("BuildJobEngineHandler")
public class BuildJobEngineHandler<T extends BaseJobContext> extends BaseJobEngineHandler<T> {

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Override
    public Integer getKey() {
        return BuildType.BUILD.getType();
    }

    @Override
    public void trackJobBuildHeartbeat(int buildId) {
        redisUtil.set(RedisKeyUtils.getJobBuildKey(buildId), true, 10);
    }

    @Override
    public void trackJobBuild(T buildJobContext) {
        BuildJobContext context = (BuildJobContext) buildJobContext;
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
                    // 判断任务是否需要中止
                    if (isAbortJobBuild(context))
                        buildWithDetails.Stop(JenkinsServerHandler.CRUMB_FLAG);
                    TimeUnit.SECONDS.sleep(TRACK_SLEEP_SECONDS); // 执行中
                } else {
                    // 任务完成
                    context.setBuildWithDetails(buildWithDetails);
                    recordJobBuild(context);
                    buildEndNotify(context);
                    break;
                }
            } catch (RetryException e) {
                log.error("重试获取JobBuild失败，jobName = {}, buildNumber = {}", context.getCsCiJob().getName(), context.getJobBuild().getEngineBuildNumber());
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

    private void recordJobBuildComputer(BuildJobContext jobBuildContext, ComputerWithDetails computerWithDetails, JobBuild jobBuild) {
        CsJobBuildExecutor pre = JobBuildExecutorBuilder.build(jobBuildContext, computerWithDetails, jobBuild);
        if (csJobBuildExecutorService.queryCsJobBuildExecutorByUniqueKey(getKey(), pre.getBuildId(), pre.getNodeName()) != null)
            return;
        recordJobBuildComputer(jobBuildContext.getJobEngine(), pre);
    }

    private boolean isAbortJobBuild(BuildJobContext context) {
        String username = (String) redisUtil.get(RedisKeyUtils.getJobBuildAbortKey(context.getJobBuild().getId()));
        if (StringUtils.isEmpty(username)) return false;
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(context.getJobBuild().getId());
        csCiJobBuild.setOperationUsername(username);
        csCiJobBuildService.updateCsCiJobBuild(csCiJobBuild);
        context.getJobBuild().setOperationUsername(username);
        return true;
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
        context.getBuildWithDetails().getArtifacts().forEach(e -> saveJobBuildArtifact((T) context, e));
        recordJobBuildChanges(context);
    }

    @Override
    protected CsJobBuildArtifact acqJobBuildArtifact(T context, Artifact artifact) {
        BuildJobContext buildJobContext = (BuildJobContext) context;
        return JobBuildArtifactBuilder.build(buildJobContext, artifact);
    }

    @Override
    protected CsOssBucket acqOssBucket(T context) {
        BuildJobContext buildJobContext = (BuildJobContext) context;
        return csOssBucketService.queryCsOssBucketById(buildJobContext.getCsCiJob().getOssBucketId());
    }

    @Override
    protected String acqOssPath(T context, CsJobBuildArtifact csJobBuildArtifact) {
        BuildJobContext buildJobContext = (BuildJobContext) context;
        IBuildJobHandler iBuildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(buildJobContext.getCsCiJob().getJobType());
       return iBuildJobHandler.acqOssPath(buildJobContext.getJobBuild(), csJobBuildArtifact);
    }

    /**
     * 变更记录
     *
     * @param context
     */
    private void recordJobBuildChanges(BuildJobContext context) {
        List<BuildChangeSet> buildChanges = context.getBuildWithDetails().getChangeSets();
        buildChanges.forEach(set -> set.getItems().forEach(e -> saveJobBuildChange(context, e)));
    }

    private void saveJobBuildChange(BuildJobContext context, BuildChangeSetItem buildChangeSetItem) {
        CsJobBuildChange pre = JobBuildChangeBuilder.build(context, buildChangeSetItem);
        if (csJobBuildChangeService.queryCsJobBuildChangeByUniqueKey(getKey(), pre.getJobId(), pre.getCommitId()) == null)
            csJobBuildChangeService.addCsJobBuildChange(pre);
    }

    private void buildEndNotify(BuildJobContext context) {
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(context.getCsCiJob().getJobType());
        dingtalkNotify.doNotify(NoticePhase.END.getType(), context);
    }


}
