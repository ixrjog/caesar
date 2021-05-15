package com.baiyi.caesar.facade.jenkins.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.packer.application.JobEnginePacker;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.facade.jenkins.JobEngineFacade;
import com.baiyi.caesar.facade.jenkins.factory.IJobEngine;
import com.baiyi.caesar.facade.jenkins.factory.JobEngineFactory;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/18 10:50 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class JobEngineFacadeImpl implements JobEngineFacade {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private JobEnginePacker ciJobEngineDecorator;

    @Override
    public void createJobEngine(int buildType, int jobId) {
        IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(buildType);
        iJobEngine.createJobEngine(jobId);
    }

    @Override
    public void updateJobEngine(int buildType, int jobId) {
        IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(buildType);
        iJobEngine.updateJobEngine(jobId);
    }

    @Override
    public boolean deleteJobBuildEngine(int jobId) {
        List<CsJobEngine> csJobEngines = csJobEngineService.queryCsJobEngineByJobId(BuildType.BUILD.getType(), jobId);
        return deleteCsJobEngines(csJobEngines);
    }

    @Override
    public boolean deleteJobDeploymentEngine(int jobId) {
        List<CsJobEngine> csJobEngines = csJobEngineService.queryCsJobEngineByJobId(BuildType.DEPLOYMENT.getType(), jobId);
        return deleteCsJobEngines(csJobEngines);
    }

    private boolean deleteCsJobEngines(List<CsJobEngine> csJobEngines) {
        for (CsJobEngine csJobEngine : csJobEngines) {
            CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(csJobEngine.getJenkinsInstanceId());
            try {
                jenkinsServerHandler.deleteJob(csJenkinsInstance.getName(), csJobEngine.getName());
            } catch (IOException e) {
                return false;
            }
            csJobEngineService.deleteCsJobEngineById(csJobEngine.getId());
        }
        return true;
    }

    @Override
    public List<JobEngineVO.JobEngine> queryJobEngines(int buildType, int jobId) {
        return csJobEngineService.queryCsJobEngineByJobId(buildType, jobId).stream().map(e ->
                ciJobEngineDecorator.wrap(BeanCopierUtil.copyProperties(e, JobEngineVO.JobEngine.class))
        ).collect(Collectors.toList());
    }

    @Override
    // @Cacheable(cacheNames = CachingConfig.CacheRepositories.COMMON, unless = "#result == null")
    public CsJenkinsInstance acqJenkinsServer(Integer jobEngineId) {
        CsJobEngine jobEngine = csJobEngineService.queryCsJobEngineById(jobEngineId);
        return csJenkinsInstanceService.queryCsJenkinsInstanceById(jobEngine.getJenkinsInstanceId());
    }


    @Override
    public String acqJenkinsServerName(Integer jobEngineId) {
        CsJobEngine jobEngine = csJobEngineService.queryCsJobEngineById(jobEngineId);
        CsJenkinsInstance instance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jobEngine.getJenkinsInstanceId());
        return instance.getName();
    }
}