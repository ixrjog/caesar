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
 * @Date 2020/8/18 3:54 下午
 * @Version 1.0
 */
@Slf4j
@Component("JavaCiJobHandler")
public class JavaBuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Override
    public String getKey() {
        return JobType.JAVA.getType();
    }

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCiJob csCiJob, JobBuildParam.BuildParam buildParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCiJob, buildParam);
        JobParamUtils.assembleJobBuildNumberParam(csCiJob, jobParamDetail);
        return jobParamDetail;
    }

    @Override
    protected boolean isLimitConcurrentJob() {
        return true;
    }


}
