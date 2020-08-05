package com.baiyi.caesar.factory.jenkins.engine;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.CiJobEngineDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.facade.jenkins.JenkinsCiJobFacade;
import com.baiyi.caesar.service.jenkins.CsCiJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/5 10:30 上午
 * @Version 1.0
 */
@Component
public class JenkinsJobEngineHandler {

    @Resource
    private CsCiJobEngineService csCiJobEngineService;

    @Resource
    private JenkinsCiJobFacade jenkinsCiJobFacade;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private CiJobEngineDecorator ciJobEngineDecorator;

    /**
     * 随机取一个可用的任务工作引擎
     * @param csCiJob
     * @return
     */
    public BusinessWrapper<CiJobVO.JobEngine> acqJobEngine(CsCiJob csCiJob) {
        List<CsCiJobEngine> csCiJobEngines = queryCiJobEngine(csCiJob.getId());
        if (CollectionUtils.isEmpty(csCiJobEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_ENGINE_NOT_CONFIGURED); // 工作引擎未配置

        List<CsCiJobEngine> activeEngines = csCiJobEngines.stream().filter(e ->
                tryJenkinsInstanceActive(e.getJenkinsInstanceId())
        ).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(activeEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_NO_ENGINES_AVAILABLE); // 没有可用的工作引擎

        return new BusinessWrapper<>(buildJobEngine(activeEngines));
    }

    private CiJobVO.JobEngine buildJobEngine(List<CsCiJobEngine> activeEngines) {
        Random random = new Random();
        int n = random.nextInt(activeEngines.size());
        CsCiJobEngine csCiJobEngine = activeEngines.get(n);
        return ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobEngine,CiJobVO.JobEngine.class));
    }

    private boolean tryJenkinsInstanceActive(int jenkinsInstanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jenkinsInstanceId);
        if (csJenkinsInstance == null) return false;
        return csJenkinsInstance.getIsActive();
    }

    private List<CsCiJobEngine> queryCiJobEngine(int ciJobId) {
        List<CsCiJobEngine> csCiJobEngines = csCiJobEngineService.queryCsCiJobEngineByJobId(ciJobId);
        if (CollectionUtils.isEmpty(csCiJobEngines))
            jenkinsCiJobFacade.createJobEngine(ciJobId);
        return csCiJobEngineService.queryCsCiJobEngineByJobId(ciJobId);
    }

}
