package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.JobBuildChangeBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.TimeUtil;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildChange;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.offbytwo.jenkins.model.BuildChangeSetItem;

/**
 * @Author baiyi
 * @Date 2020/8/10 4:58 下午
 * @Version 1.0
 */
public class JobBuildChangeBuilder {

    public static CsJobBuildChange build(BuildJobContext context, BuildChangeSetItem buildChangeItem) {
        JobBuildChangeBO bo = JobBuildChangeBO.builder()
                .buildType(context.getBuildType())
                .jobId(context.getCsCiJob().getId())
                .buildId(context.getJobBuild().getId())
                .jobName(context.getJobBuild().getJobName())
                .commitId(buildChangeItem.getCommitId())
                .commit(buildChangeItem.getComment())
                .commitMsg(buildChangeItem.getMsg())
                .commitDate(TimeUtil.acqJenkinsDate(buildChangeItem.getDate()))
                .authorFullName(buildChangeItem.getAuthor().getFullName())
                .authorAbsoluteUrl(buildChangeItem.getAuthor().getAbsoluteUrl())
                .build();
        return covert(bo);
    }

    private static CsJobBuildChange covert(JobBuildChangeBO bo) {
        return BeanCopierUtil.copyProperties(bo, CsJobBuildChange.class);
    }
}
