package com.baiyi.caesar.factory.jenkins.engine;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.offbytwo.jenkins.model.JobWithDetails;

/**
 * @Author baiyi
 * @Date 2020/8/5 10:30 上午
 * @Version 1.0
 */

public interface JenkinsJobEngineHandler {

    /**
     * 随机取一个可用的任务工作引擎
     * @param csCiJob
     * @return
     */
    BusinessWrapper<CiJobVO.JobEngine> acqJobEngine(CsCiJob csCiJob);


    void trackJobBuild(CsCiJobBuild csCiJobBuild, CiJobVO.JobEngine jobEngine, JobWithDetails job);


}
