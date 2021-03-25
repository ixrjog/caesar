package com.baiyi.caesar.facade.jenkins.factory;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/12/16 10:04 上午
 * @Version 1.0
 */
class IJobEngineTest extends BaseUnit {


    @Resource
    private CsCiJobService csCiJobService;


    @Resource
    private CsCdJobService csCdJobService;

    @Test
    void correctionCiJobEngineTest() {
        // 校正引擎
        List<CsCiJob> list = csCiJobService.selectAll();
        for (CsCiJob csCiJob : list) {
            IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(BuildType.BUILD.getType());
            BusinessWrapper<Boolean> correctionWrapper = iJobEngine.correctionJobEngine(csCiJob.getId());
        }
    }

    @Test
    void correctionCdJobEngineTest() {
        // 校正引擎
        List<CsCdJob> list = csCdJobService.selectAll();
        for (CsCdJob csCdJob : list) {
            IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(BuildType.DEPLOYMENT.getType());
            BusinessWrapper<Boolean> correctionWrapper = iJobEngine.correctionJobEngine(csCdJob.getId());
        }
    }

}