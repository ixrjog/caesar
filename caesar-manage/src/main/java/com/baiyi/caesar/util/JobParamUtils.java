package com.baiyi.caesar.util;

import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/8/18 4:03 下午
 * @Version 1.0
 */
public class JobParamUtils {


    private static final String HOST_PATTERN = "hostPattern";

    public static void invokeJobBuildNumberParam(CsCdJob csCdJob, JobParamDetail jobParamDetail) {
        jobParamDetail.getParams().put("jobBuildNumber", String.valueOf(csCdJob.getJobBuildNumber()));
    }

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

    public static void invokeOssJobUrlParam(CsCdJob csCdJob, JobParamDetail jobParamDetail) {
        if (jobParamDetail.getCsOssBucket() == null)
            return;
        // https://caesar-store.oss-cn-hangzhou.aliyuncs.com/ZEBRA-IOS-APP/ZEBRA-IOS-APP_zebra_ios_track/1/GlobalScanner.ipa
        String domain = Joiner.on(".").join(jobParamDetail.getCsOssBucket().getName(), jobParamDetail.getCsOssBucket().getExtranetEndpoint());
        String url = Joiner.on("/").join(domain, jobParamDetail.getParamByKey("applicationName"), jobParamDetail.getJobName(), String.valueOf(csCdJob.getJobBuildNumber()));
        jobParamDetail.getParams().put("ossJobUrl", "https://" + url);
    }

    public static void invokeHostPatternParam(JobParamDetail jobParamDetail, JobDeploymentParam.DeploymentParam deploymentParam) {
        if (deploymentParam.getParamMap().containsKey(HOST_PATTERN))
            jobParamDetail.getParams().put(HOST_PATTERN, deploymentParam.getParamMap().get(HOST_PATTERN));
    }

    public static void invokePodUpdate(JobParamDetail jobParamDetail, JobBuildParam.BuildQuery buildParam) {
        if (!buildParam.getParamMap().containsKey("podUpdate")) return;
        jobParamDetail.getParams().put("podUpdate", buildParam.getParamMap().get("podUpdate"));
    }

    public static void invokePubGet(JobParamDetail jobParamDetail, JobBuildParam.BuildQuery buildParam) {
        if (!buildParam.getParamMap().containsKey("pubGet")) return;
        jobParamDetail.getParams().put("pubGet", buildParam.getParamMap().get("pubGet"));
    }

    public static void invokeBuildType(JobParamDetail jobParamDetail, JobBuildParam.BuildQuery buildParam) {
        if (!buildParam.getParamMap().containsKey("BUILD_TYPE")) return;
        jobParamDetail.getParams().put("BUILD_TYPE", buildParam.getParamMap().get("BUILD_TYPE"));
    }

    public static void invokeProductFlavor(JobParamDetail jobParamDetail, JobBuildParam.BuildQuery buildParam) {
        if (!buildParam.getParamMap().containsKey("PRODUCT_FLAVOR")) return;
        jobParamDetail.getParams().put("PRODUCT_FLAVOR", buildParam.getParamMap().get("PRODUCT_FLAVOR"));
    }


}
