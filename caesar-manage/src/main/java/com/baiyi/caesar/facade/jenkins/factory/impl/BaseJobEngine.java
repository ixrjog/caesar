package com.baiyi.caesar.facade.jenkins.factory.impl;

import com.baiyi.caesar.decorator.application.ApplicationEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.jenkins.factory.IJobEngine;
import com.baiyi.caesar.facade.jenkins.factory.JobEngineFactory;
import com.baiyi.caesar.factory.jenkins.engine.JobEngineHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/18 11:34 上午
 * @Version 1.0
 */
@Component
public abstract class BaseJobEngine<T> implements IJobEngine, InitializingBean {

    @Resource
    protected CsApplicationService csApplicationService;

    @Resource
    protected CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private ApplicationEngineDecorator applicationEngineDecorator;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    protected CsJobEngineService csJobEngineService;

    @Resource
    protected JobEngineHandler jobEngineHandler;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    abstract protected T acqJob(int jobId);

    abstract protected CsApplication acqApplication(T job);

    abstract boolean tryJenkinsEngine(int jobId, ApplicationVO.Engine engine);

    @Override
    public void createJobEngine(int jobId) {
        T job = acqJob(jobId);
        CsApplication csApplication = acqApplication(job);
        List<ApplicationVO.Engine> engines = acqEngines(csApplication);
        engines.forEach(e -> createJobEngine(csApplication, job, e));
    }

    @Override
    public void updateJobEngine(int jobId) {
        T job = acqJob(jobId);
        CsApplication csApplication = acqApplication(job);
        List<CsJobEngine> engines = csJobEngineService.queryCsJobEngineByJobId(getKey(), jobId);
        if (CollectionUtils.isEmpty(engines)) return;
        engines.forEach(e -> updateJobEngine(csApplication, job, e));
    }

    abstract protected void updateJobEngine(CsApplication csApplication, T job, CsJobEngine engine);

    abstract protected void createJobEngine(CsApplication csApplication, T job, ApplicationVO.Engine engine);

    protected void createJobEngine(CsJobEngine csJobEngine, int jobTplId, ApplicationVO.Engine engine) throws IOException {
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(jobTplId);
        if (csJobTpl == null) return;
        jenkinsServerHandler.createJob(engine.getInstance().getName(), csJobEngine.getName(), csJobTpl.getTplContent());
        if (jenkinsServerHandler.getJob(engine.getInstance().getName(), csJobEngine.getName()) != null) {
            csJobEngine.setTplVersion(csJobTpl.getTplVersion());
            csJobEngine.setTplHash(csJobTpl.getTplHash());
        }
    }

    protected void updateJobEngine(CsJobEngine csJobEngine, int jobTplId, String jenkinsInstanceName) throws IOException {
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(jobTplId);
        if (csJobTpl == null) return;
        jenkinsServerHandler.updateJob(jenkinsInstanceName, csJobEngine.getName(), csJobTpl.getTplContent());
        csJobEngine.setTplVersion(csJobTpl.getTplVersion());
        csJobEngine.setTplHash(csJobTpl.getTplHash());
    }

    protected List<ApplicationVO.Engine> acqEngines(CsApplication csApplication) {
        List<ApplicationVO.Engine> engines;
        if (csApplication.getEngineType() == 0) {
            engines = csJenkinsInstanceService.queryAll()
                    .stream().map(e -> {
                        ApplicationVO.Engine engine = new ApplicationVO.Engine();
                        engine.setApplicationId(csApplication.getId());
                        engine.setJenkinsInstanceId(e.getId());
                        return applicationEngineDecorator.decorator(engine, 1);
                    }).collect(Collectors.toList());
        } else {
            // 应用引擎配置
            engines = applicationFacade.queryApplicationEngineByApplicationId(csApplication.getId());
        }
        return engines;
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        JobEngineFactory.register(this);
    }

}
