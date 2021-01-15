package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.CiJobBuildBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.google.common.base.Joiner;
import org.gitlab.api.models.GitlabBranch;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:33 下午
 * @Version 1.0
 */
public class CiJobBuildBuilder {

    public static CsCiJobBuild build(CsApplication csApplication, CsCiJob csCiJob, JobEngineVO.JobEngine jobEngine, JobParamDetail jobParamDetail, GitlabBranch gitlabBranch,
                                     String username, Boolean isSilence) {
        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());
        Integer engineBuildNumber = jobEngine.getNextBuildNumber() == 0 ? 1 : jobEngine.getNextBuildNumber();
        String versionName = StringUtils.isEmpty(jobParamDetail.getVersionName()) ? Joiner.on("-").join("release", csCiJob.getJobBuildNumber()) : jobParamDetail.getVersionName();

        CiJobBuildBO bo = CiJobBuildBO.builder()
                .applicationId(csApplication.getId())
                .jobName(jobName)
                .ciJobId(csCiJob.getId())
                .username(username)
                .branch(jobParamDetail.getParams().getOrDefault("branch", ""))
                .jobEngineId(jobEngine.getId())
                .jobBuildNumber(csCiJob.getJobBuildNumber())
                .engineBuildNumber(engineBuildNumber)
                .commit(gitlabBranch != null ? gitlabBranch.getCommit().getId() : "")
                .versionName(versionName)
                .versionDesc(jobParamDetail.getVersionDesc())
                .isSilence(isSilence != null ? isSilence : false)
                .isRollback(jobParamDetail.getIsRollback())
                .build();
        return covert(bo);
    }

    private static CsCiJobBuild covert(CiJobBuildBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsCiJobBuild.class);
    }
}
