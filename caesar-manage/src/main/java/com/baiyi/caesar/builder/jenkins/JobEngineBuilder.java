package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.JobEngineBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/8/3 5:08 下午
 * @Version 1.0
 */
public class JobEngineBuilder {

    private static final String JOB_URL = "job";

    public static CsJobEngine build(CsApplication csApplication, CsCiJob csCiJob, ApplicationVO.Engine engine) {
        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());

        JobEngineBO bo = JobEngineBO.builder()
                .buildType(BuildType.BUILD.getType())
                .jobId(csCiJob.getId())
                .jenkinsInstanceId(engine.getJenkinsInstanceId())
                .name(jobName)
                .jobUrl(Joiner.on("/").join(engine.getInstance().getUrl(), JOB_URL, jobName))
                .build();
        return covert(bo);
    }

    public static CsJobEngine build(CsApplication csApplication, CsCdJob csCdJob, ApplicationVO.Engine engine) {
        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCdJob.getJobKey());

        JobEngineBO bo = JobEngineBO.builder()
                .buildType(BuildType.DEPLOYMENT.getType())
                .jobId(csCdJob.getId())
                .jenkinsInstanceId(engine.getJenkinsInstanceId())
                .name(jobName)
                .jobUrl(Joiner.on("/").join(engine.getInstance().getUrl(), JOB_URL, jobName))
                .build();
        return covert(bo);
    }


    private static CsJobEngine covert(JobEngineBO bo) {
        return BeanCopierUtil.copyProperties(bo, CsJobEngine.class);
    }
}
