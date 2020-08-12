package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.CiJobBuildChangeBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.TimeUtils;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildChange;
import com.baiyi.caesar.factory.jenkins.context.JobBuildContext;
import com.offbytwo.jenkins.model.BuildChangeSetItem;

/**
 * @Author baiyi
 * @Date 2020/8/10 4:58 下午
 * @Version 1.0
 */
public class CiJobBuildChangeBuilder {

    public static CsCiJobBuildChange build(JobBuildContext jobBuildContext, BuildChangeSetItem buildChangeItem) {

        CiJobBuildChangeBO bo = CiJobBuildChangeBO.builder()
                .ciJobId(jobBuildContext.getCsCiJob().getId())
                .buildId(jobBuildContext.getCsCiJobBuild().getId())
                .jobName(jobBuildContext.getCsCiJobBuild().getJobName())
                .commitId(buildChangeItem.getCommitId())
                .commit(buildChangeItem.getComment())
                .commitMsg(buildChangeItem.getMsg())
                .commitDate(TimeUtils.acqJenkinsDate(buildChangeItem.getDate()))
                .authorFullName(buildChangeItem.getAuthor().getFullName())
                .authorAbsoluteUrl(buildChangeItem.getAuthor().getAbsoluteUrl())
                .build();
        return covert(bo);
    }

    private static CsCiJobBuildChange covert(CiJobBuildChangeBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsCiJobBuildChange.class);
    }
}
