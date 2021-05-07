package com.baiyi.caesar.factory.jenkins.impl.build;

import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.builder.JenkinsJobParamsBuilder;
import com.baiyi.caesar.factory.jenkins.builder.JenkinsJobParamsMap;
import com.baiyi.caesar.jenkins.context.JobParametersContext;
import com.baiyi.caesar.util.JobParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.caesar.common.base.Build.*;
import static com.baiyi.caesar.common.base.Build.OSS_JOB_URL;

/**
 * @Author baiyi
 * @Date 2021/5/7 10:08 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AndroidAARBuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Override
    public String getKey() {
        return JobTypeEnum.ANDROID_AAR.getType();
    }

    @Override
    protected JobParametersContext buildJobParametersContext(CsCiJob csCiJob, JobBuildParam.BuildParam buildParam) {
        JobParametersContext context = super.buildJobParametersContext(csCiJob, buildParam);
        JenkinsJobParamsMap jenkinsJobParamsMap = JenkinsJobParamsBuilder.newBuilder()
                .paramEntry(JOB_BUILD_NUMBER, String.valueOf(csCiJob.getJobBuildNumber()))
                .paramEntry(OSS_JOB_URL, JobParamUtils.getOssJobUrl(csCiJob.getJobBuildNumber(), context))
                .build();
        context.putParams(jenkinsJobParamsMap.getParams());

        return context;
    }
}
