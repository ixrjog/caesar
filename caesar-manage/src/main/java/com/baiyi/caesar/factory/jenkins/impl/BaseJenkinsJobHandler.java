package com.baiyi.caesar.factory.jenkins.impl;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.factory.jenkins.IJenkinsJobHandler;
import com.baiyi.caesar.factory.jenkins.JenkinsJobHandlerFactory;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/5 9:46 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseJenkinsJobHandler implements IJenkinsJobHandler, InitializingBean {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsCiJobService csCiJobService;

    @Override
    public BusinessWrapper<Boolean> build(JobBuildParam.CiJobBuild ciJobBuild) {

        JobWithDetails job = jenkinsServerHandler.getJob("aa", "bb");

        try {
            job.build(null, JenkinsServerHandler.CRUMB_FLAG);
        } catch (IOException e) {

        }

        return BusinessWrapper.SUCCESS;
    }


    private void ddd(int ciJobId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(ciJobId);
    }


    private QueueReference build(JobWithDetails job, Map<String, String> params) throws IOException {
        return job.build(params, JenkinsServerHandler.CRUMB_FLAG);
    }


    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        JenkinsJobHandlerFactory.register(this);
    }

}
