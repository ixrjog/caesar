package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.factory.jenkins.IJenkinsJobHandler;
import com.baiyi.caesar.factory.jenkins.JenkinsJobHandlerFactory;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/6 3:51 下午
 * @Version 1.0
 */
@Component
public class JobFacade {

    @Resource
    private CsCiJobService csCiJobService;

    public BusinessWrapper<Boolean> buildCiJob(JobBuildParam.CiBuildParam buildParam) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById((buildParam.getCiJobId()));
        IJenkinsJobHandler jenkinsJobHandler = JenkinsJobHandlerFactory.getJenkinsJobBuildByKey(csCiJob.getJobType());
        jenkinsJobHandler.build(csCiJob, buildParam);
        return BusinessWrapper.SUCCESS;
    }


}
