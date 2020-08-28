package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.application.CdJobVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/27 5:49 下午
 * @Version 1.0
 */
@Component
public class CdJobEngineDecorator {

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    public CdJobVO.JobEngine decorator(CdJobVO.JobEngine jobEngine) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jobEngine.getJenkinsInstanceId());
        if (csJenkinsInstance != null) {
            jobEngine.setJenkinsInstance(BeanCopierUtils.copyProperties(csJenkinsInstance, JenkinsInstanceVO.Instance.class));
        }

        if (jobEngine.getLastBuildNumber() == 0) {
            jobEngine.setLastBuildUrl(jobEngine.getJobUrl());
        } else {
            jobEngine.setLastBuildUrl(Joiner.on("/").join(jobEngine.getJobUrl(), jobEngine.getLastBuildNumber()));
        }
        return jobEngine;
    }
}
