package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.CiJobBuildArtifactBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.offbytwo.jenkins.model.Artifact;

/**
 * @Author baiyi
 * @Date 2020/8/8 9:41 上午
 * @Version 1.0
 */
public class CiJobBuildArtifactBuilder {

    public static CsCiJobBuildArtifact build(JobBuildContext jobBuildContext, Artifact artifact) {

        CiJobBuildArtifactBO bo = CiJobBuildArtifactBO.builder()
                .ciJobId(jobBuildContext.getCsCiJob().getId())
                .buildId(jobBuildContext.getJobBuild().getId())
                .jobName(jobBuildContext.getJobBuild().getJobName())
                .artifactDisplayPath(artifact.getDisplayPath())
                .artifactRelativePath(artifact.getRelativePath())
                .artifactFileName(artifact.getFileName())
                .build();
        return covert(bo);
    }

    private static CsCiJobBuildArtifact covert(CiJobBuildArtifactBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsCiJobBuildArtifact.class);
    }
}
