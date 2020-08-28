package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.decorator.application.ApplicationEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/27 5:43 下午
 * @Version 1.0
 */
@Component
public class JenkinsCdJobFacade {

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private ApplicationEngineDecorator applicationEngineDecorator;

    /**
     * 创建Job引擎配置
     *
     * @param cdJobId
     */
    public void createJobEngine(int cdJobId) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(cdJobId);
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csCdJob.getApplicationId());
        List<ApplicationVO.Engine> engines;
        if (csApplication.getEngineType() == 0) {
            engines = csJenkinsInstanceService.queryAll()
                    .stream().map(e -> {
                        ApplicationVO.Engine engine = new ApplicationVO.Engine();
                        engine.setApplicationId(csApplication.getId());
                        engine.setJenkinsInstanceId(e.getId());
                        return  applicationEngineDecorator.decorator(engine,1);
                    }).collect(Collectors.toList());
        } else {
            // 应用引擎配置
            engines = applicationFacade.queryApplicationEngineByApplicationId(csCdJob.getApplicationId());
        }
        engines.forEach(e ->
                createJobEngine(csApplication, csCdJob, e)
        );
    }

    private void createJobEngine(CsApplication csApplication, CsCdJob csCdJob, ApplicationVO.Engine engine) {
//        CsCdJobEngine pre = csCdJobEngineService.queryCsCdJobEngineByUniqueKey(csCdJob.getId(), engine.getJenkinsInstanceId());
//        if (pre != null) return;
//        CsCdJobEngine csCdJobEngine = CdJobEngineBuilder.build(csApplication, csCdJob, engine);
//        csCdJobEngineService.addCsCdJobEngine(csCdJobEngine);
//        // 创建JenkinsJob
//        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(csCdJob.getJobTplId());
//        if (csJobTpl != null) {
//            try {
//                jenkinsServerHandler.createJob(engine.getInstance().getName(), csCdJobEngine.getName(), csJobTpl.getTplContent());
//                if (jenkinsServerHandler.getJob(engine.getInstance().getName(), csCdJobEngine.getName()) != null) {
//                    csCdJobEngine.setTplVersion(csJobTpl.getTplVersion());
//                    csCdJobEngine.setTplHash(csJobTpl.getTplHash());
//                    csCdJobEngineService.updateCsCdJobEngine(csCdJobEngine);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


}
