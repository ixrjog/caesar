package com.baiyi.caesar.util;

import com.baiyi.caesar.jenkins.context.JobParametersContext;
import com.google.common.base.Joiner;

import static com.baiyi.caesar.common.base.Build.APPLICATION_NAME;

/**
 * @Author baiyi
 * @Date 2020/8/18 4:03 下午
 * @Version 1.0
 */
public class JobParamUtils {

    /**
     * https://caesar-store.oss-cn-hangzhou.aliyuncs.com/ZEBRA-IOS-APP/ZEBRA-IOS-APP_zebra_ios_track/1/GlobalScanner.ipa
     * @param jobBuildNumber
     * @param jobParamDetail
     * @return
     */
    public static String getOssJobUrl(Integer jobBuildNumber, JobParametersContext jobParamDetail) {
        return getOssJobUrl(String.valueOf(jobBuildNumber), jobParamDetail);
    }

    public static String getOssJobUrl(String jobBuildNumber, JobParametersContext jobParamDetail) {
        if (jobParamDetail.getCsOssBucket() == null)
            return null;
        String domain = Joiner.on(".").join(jobParamDetail.getCsOssBucket().getName(), jobParamDetail.getCsOssBucket().getExtranetEndpoint());
        return  "https://" + Joiner.on("/").join(domain, jobParamDetail.getParamByKey(APPLICATION_NAME), jobParamDetail.getJobName(), jobBuildNumber);
    }

}
