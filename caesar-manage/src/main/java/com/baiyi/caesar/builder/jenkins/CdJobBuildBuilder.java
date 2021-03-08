package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.CdJobBuildBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.jenkins.context.JobParametersContext;
import com.google.common.base.Joiner;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/8/29 2:07 下午
 * @Version 1.0
 */
public class CdJobBuildBuilder {

    public static CsCdJobBuild build(CsApplication csApplication, CsCdJob csCdJob, JobEngineVO.JobEngine jobEngine, JobParametersContext jobParamDetail, int ciBuildId) {
        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCdJob.getJobKey());
        Integer engineBuildNumber = jobEngine.getNextBuildNumber() == 0 ? 1 : jobEngine.getNextBuildNumber();
        String versionName = StringUtils.isEmpty(jobParamDetail.getVersionName()) ? Joiner.on("-").join("release", csCdJob.getJobBuildNumber()) : jobParamDetail.getVersionName();

        CdJobBuildBO bo = CdJobBuildBO.builder()
                .applicationId(csApplication.getId())
                .jobName(jobName)
                .ciBuildId(ciBuildId)
                .cdJobId(csCdJob.getId())
                .username(SessionUtil.getUsername())
                .jobEngineId(jobEngine.getId())
                .jobBuildNumber(csCdJob.getJobBuildNumber())
                .engineBuildNumber(engineBuildNumber)
                .versionName(versionName)
                .versionDesc(jobParamDetail.getVersionDesc())
                .build();
        return covert(bo);
    }

    private static CsCdJobBuild covert(CdJobBuildBO bo) {
        return BeanCopierUtil.copyProperties(bo, CsCdJobBuild.class);
    }
}
