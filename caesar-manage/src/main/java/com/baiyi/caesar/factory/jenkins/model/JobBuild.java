package com.baiyi.caesar.factory.jenkins.model;

import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.baiyi.caesar.jenkins.context.JobDeploymentContext;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/8/12 5:07 下午
 * @Version 1.0
 */
@Data
@Builder
public class JobBuild {

    private String jobUrl;
    private String jobName;
    private Integer buildNumber;


    public boolean isJobBuild(JobBuildContext context) {
        return context.getJobBuild().getJobName().equals(this.jobName) && context.getJobBuild().getEngineBuildNumber().equals(buildNumber);
    }

    public boolean isJobBuild(JobDeploymentContext context) {
        return context.getJobBuild().getJobName().equals(this.jobName) && context.getJobBuild().getEngineBuildNumber().equals(buildNumber);
    }

}
