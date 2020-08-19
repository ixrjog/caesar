package com.baiyi.caesar.util;

import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.factory.jenkins.model.JobParamDetail;

/**
 * @Author baiyi
 * @Date 2020/8/18 4:03 下午
 * @Version 1.0
 */
public class JobParamUtils {

    public static void invokeJobBuildNumberParam(CsCiJob csCiJob, JobParamDetail jobParamDetail){
        jobParamDetail.getParams().put("jobBuildNumber", String.valueOf(csCiJob.getJobBuildNumber()));
    }

}
