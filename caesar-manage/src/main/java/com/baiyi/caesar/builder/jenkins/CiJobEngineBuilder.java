package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.CiJobEngineBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobEngine;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/8/3 5:08 下午
 * @Version 1.0
 */
public class CiJobEngineBuilder {

    private static final String JOB_URL = "job";

    public static CsCiJobEngine build(CsApplication csApplication, CsCiJob csCiJob, ApplicationVO.Engine engine) {
        String jobName = Joiner.on("_").join(csApplication.getApplicationKey(), csCiJob.getJobKey());

        CiJobEngineBO bo = CiJobEngineBO.builder()
                .ciJobId(csCiJob.getId())
                .jenkinsInstanceId(engine.getJenkinsInstanceId())
                .name(jobName)
                .jobUrl(Joiner.on("/").join(engine.getInstance().getUrl(), JOB_URL, jobName))
                .build();
        return covert(bo);
    }


    private static CsCiJobEngine covert(CiJobEngineBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsCiJobEngine.class);
    }
}
