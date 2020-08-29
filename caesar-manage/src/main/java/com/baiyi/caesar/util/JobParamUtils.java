package com.baiyi.caesar.util;

import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/8/18 4:03 下午
 * @Version 1.0
 */
public class JobParamUtils {

    public static void invokeJobBuildNumberParam(CsCiJob csCiJob, JobParamDetail jobParamDetail) {
        jobParamDetail.getParams().put("jobBuildNumber", String.valueOf(csCiJob.getJobBuildNumber()));
    }

    public static void invokeOssJobUrlParam(CsCiJob csCiJob, JobParamDetail jobParamDetail) {
        if (jobParamDetail.getCsOssBucket() == null)
            return;
        // https://caesar-store.oss-cn-hangzhou.aliyuncs.com/ZEBRA-IOS-APP/ZEBRA-IOS-APP_zebra_ios_track/1/GlobalScanner.ipa
        String domain = Joiner.on(".").join(jobParamDetail.getCsOssBucket().getName(), jobParamDetail.getCsOssBucket().getExtranetEndpoint());
        String url = Joiner.on("/").join(domain, jobParamDetail.getParamByKey("applicationName"), jobParamDetail.getJobName(), String.valueOf(csCiJob.getJobBuildNumber()));
        jobParamDetail.getParams().put("ossJobUrl", "https://" + url);
    }

    public static void invokePodUpdate(JobParamDetail jobParamDetail, JobBuildParam.BuildParam buildParam) {
        if (!buildParam.getParamMap().containsKey("podUpdate")) return;
        jobParamDetail.getParams().put("podUpdate", buildParam.getParamMap().get("podUpdate"));
    }

    public static void invokeEnvironmentBuild(JobParamDetail jobParamDetail, JobBuildParam.BuildParam buildParam) {
        if (!buildParam.getParamMap().containsKey("ENVIRONMENT_BUILD")) return;
        jobParamDetail.getParams().put("ENVIRONMENT_BUILD", buildParam.getParamMap().get("ENVIRONMENT_BUILD"));
    }

    public static void invokeProductFlavorBuild(JobParamDetail jobParamDetail, JobBuildParam.BuildParam buildParam) {
        if (!buildParam.getParamMap().containsKey("PRODUCT_FLAVOR_BUILD")) return;
        jobParamDetail.getParams().put("PRODUCT_FLAVOR_BUILD", buildParam.getParamMap().get("PRODUCT_FLAVOR_BUILD"));
    }


}
