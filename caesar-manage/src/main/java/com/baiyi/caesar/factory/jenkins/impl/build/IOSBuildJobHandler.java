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

/**
 * @Author baiyi
 * @Date 2020/8/19 4:23 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class IOSBuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Override
    public String getKey() {
        return JobTypeEnum.IOS.getType();
    }

    @Override
    protected JobParametersContext buildJobParametersContext(CsCiJob csCiJob, JobBuildParam.BuildParam buildParam) {
        JobParametersContext context = super.buildJobParametersContext(csCiJob, buildParam);

        JenkinsJobParamsMap jenkinsJobParamsMap = JenkinsJobParamsBuilder.newBuilder()
                .paramEntry(BUILD_TYPE, buildParam)
                .paramEntry(POD_UPDATE, buildParam)
                .paramEntry(PUB_GET, buildParam)
                .paramEntry(JOB_BUILD_NUMBER, String.valueOf(csCiJob.getJobBuildNumber()))
                .paramEntry(OSS_JOB_URL, JobParamUtils.getOssJobUrl(csCiJob.getJobBuildNumber(), context))
                .build();
        context.putParams(jenkinsJobParamsMap.getParams());

        return context;
    }

}
