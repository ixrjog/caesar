package com.baiyi.caesar.factory.jenkins.impl.build;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.monitor.MonitorHandler;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/18 3:54 下午
 * @Version 1.0
 */
@Slf4j
@Component("JavaCiJobHandler")
public class JavaBuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Resource
    private MonitorHandler monitorHandler;

    @Override
    public String getKey() {
        return JobType.JAVA.getType();
    }

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCiJob csCiJob, JobBuildParam.BuildParam buildParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCiJob, buildParam);
        JobParamUtils.assembleJobBuildNumberParam(csCiJob, jobParamDetail);
        JobParamUtils.assembleIsSonarParam(jobParamDetail, buildParam);
        return jobParamDetail;
    }

    @Override
    protected void updateHostStatus(CsApplication csApplication, Map<String, String> params, int status) {
        monitorHandler.updateHostStatus(csApplication, params, status);
    }


    @Override
    protected boolean isLimitConcurrentJob() {
        return true;
    }


}
