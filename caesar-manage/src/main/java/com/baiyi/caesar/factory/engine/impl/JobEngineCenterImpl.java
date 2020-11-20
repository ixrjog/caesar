package com.baiyi.caesar.factory.engine.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.factory.engine.IJobEngineHandler;
import com.baiyi.caesar.factory.engine.JobEngineCenter;
import com.baiyi.caesar.factory.engine.JobEngineHandlerFactory;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/8/6 10:56 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class JobEngineCenterImpl implements JobEngineCenter {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JobEngineDecorator ciJobEngineDecorator;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Override
    public JobEngineVO.JobEngine acqJobEngineByJobEngineId(int jobEngineId) {
        CsJobEngine csJobEngine = csJobEngineService.queryCsJobEngineById(jobEngineId);
        return ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csJobEngine, JobEngineVO.JobEngine.class));
    }

    @Override
    public boolean tryJenkinsInstanceActive(int jenkinsInstanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jenkinsInstanceId);
        if (csJenkinsInstance == null) return false;
        if (!csJenkinsInstance.getIsActive())
            return false;
        // 校验实例是否正常
        return jenkinsServerHandler.isActive(csJenkinsInstance.getName());
    }


    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void trackJobBuild(BuildJobContext context) {
        IJobEngineHandler iJobEngineHandler = JobEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.BUILD.getType()); // 追踪任务
        iJobEngineHandler.trackJobBuild(context);
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void trackJobBuild(DeploymentJobContext context) {
        IJobEngineHandler iJobEngineHandler = JobEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.DEPLOYMENT.getType()); // 追踪任务
        iJobEngineHandler.trackJobBuild(context);
    }


}