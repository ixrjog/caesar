package com.baiyi.caesar.jenkins;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/6 4:01 下午
 * @Version 1.0
 */
public class JobTest  extends BaseUnit {

    @Resource
    private JobFacade jobFacade;

    @Test
    void buildTest() {
        JobBuildParam.CiBuildParam buildParam = new JobBuildParam.CiBuildParam();
        buildParam.setCiJobId(2);
        jobFacade.buildCiJob( buildParam);
    }
}
