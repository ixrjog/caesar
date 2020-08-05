package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.CiJobBuildBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:33 下午
 * @Version 1.0
 */
public class CiJobBuildBuilder {

    public static CsCiJobBuild build(CsApplication csApplication, CsCiJob csCiJob, CiJobVO.JobEngine jobEngine) {
        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());

        CiJobBuildBO bo = CiJobBuildBO.builder()
                .applicationId(csApplication.getId())
                .jobName(jobName)
                .ciJobId(csCiJob.getId())
                .jobEngineId(jobEngine.getId())
                .jobBuildNumber(csCiJob.getJobBuildNumber() + 1)
                .engineBuildNumber(jobEngine.getNextBuildNumber() == 0 ? 1 : jobEngine.getNextBuildNumber())
                .build();
        return covert(bo);
    }

    private static CsCiJobBuild covert(CiJobBuildBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsCiJobBuild.class);
    }
}
