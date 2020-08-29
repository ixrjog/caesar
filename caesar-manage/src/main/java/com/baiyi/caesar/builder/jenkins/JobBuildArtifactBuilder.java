package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.JobBuildArtifactBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.baiyi.caesar.jenkins.context.JobDeploymentContext;
import com.offbytwo.jenkins.model.Artifact;

/**
 * @Author baiyi
 * @Date 2020/8/8 9:41 上午
 * @Version 1.0
 */
public class JobBuildArtifactBuilder {

    public static CsJobBuildArtifact build(JobDeploymentContext context, Artifact artifact) {
        JobBuildArtifactBO bo = JobBuildArtifactBO.builder()
                .buildType(context.getBuildType())
                .jobId(context.getCsCdJob().getId())
                .buildId(context.getJobBuild().getId())
                .jobName(context.getJobBuild().getJobName())
                .artifactDisplayPath(artifact.getDisplayPath())
                .artifactRelativePath(artifact.getRelativePath())
                .artifactFileName(artifact.getFileName())
                .build();
        return covert(bo);
    }


    public static CsJobBuildArtifact build(JobBuildContext context, Artifact artifact) {
        JobBuildArtifactBO bo = JobBuildArtifactBO.builder()
                .buildType(context.getBuildType())
                .jobId(context.getCsCiJob().getId())
                .buildId(context.getJobBuild().getId())
                .jobName(context.getJobBuild().getJobName())
                .artifactDisplayPath(artifact.getDisplayPath())
                .artifactRelativePath(artifact.getRelativePath())
                .artifactFileName(artifact.getFileName())
                .build();
        return covert(bo);
    }

    private static CsJobBuildArtifact covert(JobBuildArtifactBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsJobBuildArtifact.class);
    }
}
