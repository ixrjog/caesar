package com.baiyi.caesar.factory.jenkins.impl.build;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/8/19 4:23 下午
 * @Version 1.0
 */
@Slf4j
@Component("IOSCiJobHandler")
public class IOSBuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Override
    public String getKey() {
        return JobType.IOS.getType();
    }

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCiJob csCiJob, JobBuildParam.BuildQuery buildParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCiJob, buildParam);
        JobParamUtils.invokeJobBuildNumberParam(csCiJob, jobParamDetail);
        JobParamUtils.invokeOssJobUrlParam(csCiJob, jobParamDetail);
        JobParamUtils.invokePodUpdate(jobParamDetail, buildParam);
        return jobParamDetail;
    }

}
