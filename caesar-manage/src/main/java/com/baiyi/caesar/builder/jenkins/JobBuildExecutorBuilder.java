package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.JobBuildExecutorBO;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildExecutor;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.google.common.base.Joiner;
import com.offbytwo.jenkins.model.ComputerWithDetails;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/12 4:34 下午
 * @Version 1.0
 */
public class JobBuildExecutorBuilder {
    //  CiJobBuildExecutorBO

    private static final String DISK_SPACE_MONITOR_KEY = "hudson.node_monitors.DiskSpaceMonitor";

    public static CsJobBuildExecutor build(JobBuildContext jobBuildContext, ComputerWithDetails computerWithDetails, JobBuild jobBuild) {
        Map<String, Map> monitorData = computerWithDetails.getMonitorData();
        Map<String, Map> diskMonitorMap ;
        String rootDirectory = "";
        if (monitorData.containsKey(DISK_SPACE_MONITOR_KEY)) {
            diskMonitorMap = monitorData.get(DISK_SPACE_MONITOR_KEY);
            Object o = diskMonitorMap.getOrDefault("path", null);
            if (o != null)
                rootDirectory = String.valueOf(o);
        }
        JobBuildExecutorBO bo = JobBuildExecutorBO.builder()
                .buildType(BuildType.BUILD.getType())
                .jobId(jobBuildContext.getCsCiJob().getId())
                .buildId(jobBuildContext.getJobBuild().getId())
                .jobName(jobBuildContext.getJobBuild().getJobName())
                .nodeName(computerWithDetails.getDisplayName())
                .rootDirectory(rootDirectory)
                .workspace(Joiner.on("/").join(rootDirectory,"workspace",jobBuildContext.getJobBuild().getJobName()))
                .jobUrl(jobBuild.getJobUrl())
                .buildNumber(jobBuild.getBuildNumber())
                .build();
        return covert(bo);
    }

    private static CsJobBuildExecutor covert(JobBuildExecutorBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsJobBuildExecutor.class);
    }
}
