package com.baiyi.caesar.packer.application;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/4 11:46 上午
 * @Version 1.0
 */
@Component
public class JobEnginePacker {

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    public JobEngineVO.JobEngine wrap(JobEngineVO.JobEngine jobEngine) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jobEngine.getJenkinsInstanceId());
        if (csJenkinsInstance != null) {
            jobEngine.setJenkinsInstance(BeanCopierUtil.copyProperties(csJenkinsInstance, JenkinsInstanceVO.Instance.class));
        }

        if (jobEngine.getLastBuildNumber() == 0) {
            jobEngine.setLastBuildUrl(jobEngine.getJobUrl());
        } else {
            jobEngine.setLastBuildUrl(Joiner.on("/").join(jobEngine.getJobUrl(), jobEngine.getLastBuildNumber()));
        }
        return jobEngine;
    }
}
