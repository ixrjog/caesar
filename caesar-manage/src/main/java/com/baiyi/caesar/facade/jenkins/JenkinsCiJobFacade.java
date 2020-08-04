package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.builder.jenkins.CiJobEngineBuilder;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.CiJobEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.CsCiJobEngineService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/3 4:33 下午
 * @Version 1.0
 */
@Component
public class JenkinsCiJobFacade {

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCiJobEngineService csCiJobEngineService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CiJobEngineDecorator ciJobEngineDecorator;

    /**
     * 创建Job引擎配置
     *
     * @param ciJobId
     */
    public void createJobEngine(int ciJobId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(ciJobId);
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCiJob.getApplicationId());

        // 应用引擎配置
        List<ApplicationVO.Engine> engines = applicationFacade.queryApplicationEngineByApplicationId(csCiJob.getApplicationId());

        engines.forEach(e -> {
            createJobEngine(csApplication, csCiJob, e);
        });
        // List<CsCiJobEngine> csCiJobEngines = csCiJobEngineService.queryCsCiJobEngineByJobId(ciJobId);
    }

    private void createJobEngine(CsApplication csApplication, CsCiJob csCiJob, ApplicationVO.Engine engine) {
        CsCiJobEngine pre = csCiJobEngineService.queryCsCiJobEngineByUniqueKey(csCiJob.getId(), engine.getJenkinsInstanceId());
        if (pre != null) return;
        CsCiJobEngine csCiJobEngine = CiJobEngineBuilder.build(csApplication, csCiJob, engine);
        csCiJobEngineService.addCsCiJobEngine(csCiJobEngine);
        // 创建JenkinsJob
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(csCiJob.getJobTplId());
        if (csJobTpl != null) {
            try {
                jenkinsServerHandler.createJob(engine.getInstance().getName(), csCiJobEngine.getName(), csJobTpl.getTplContent());
                if (jenkinsServerHandler.getJob(engine.getInstance().getName(), csCiJobEngine.getName()) != null) {
                    csCiJobEngine.setTplVersion(csJobTpl.getTplVersion());
                    csCiJobEngine.setTplHash(csJobTpl.getTplHash());
                    csCiJobEngineService.updateCsCiJobEngine(csCiJobEngine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<CiJobVO.JobEngine> queryJobEngine(int ciJobId) {
        List<CsCiJobEngine> list = csCiJobEngineService.queryCsCiJobEngineByJobId(ciJobId);
        return list.stream().map(e ->
                ciJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(e, CiJobVO.JobEngine.class))
        ).collect(Collectors.toList());
    }
}
