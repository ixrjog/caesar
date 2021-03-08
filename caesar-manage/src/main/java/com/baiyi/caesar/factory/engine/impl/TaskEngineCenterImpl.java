package com.baiyi.caesar.factory.engine.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.factory.engine.ITaskEngineHandler;
import com.baiyi.caesar.factory.engine.TaskEngineCenter;
import com.baiyi.caesar.factory.engine.TaskEngineHandlerFactory;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/6 10:56 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class TaskEngineCenterImpl implements TaskEngineCenter {

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
        return ciJobEngineDecorator.decorator(BeanCopierUtil.copyProperties(csJobEngine, JobEngineVO.JobEngine.class));
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
    @Async(value = Global.TaskPools.COMMON)
    public void trackBuildTask(BuildJobContext context) {
        ITaskEngineHandler iTaskEngineHandler = TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.BUILD.getType()); // 追踪任务
        iTaskEngineHandler.trackJobBuild(context);
    }

    @Override
    @Async(value = Global.TaskPools.COMMON)
    public void trackBuildTask(DeploymentJobContext context) {
        ITaskEngineHandler iTaskEngineHandler = TaskEngineHandlerFactory.getIJobEngineHandlerByKey(BuildType.DEPLOYMENT.getType()); // 追踪任务
        iTaskEngineHandler.trackJobBuild(context);
    }


}