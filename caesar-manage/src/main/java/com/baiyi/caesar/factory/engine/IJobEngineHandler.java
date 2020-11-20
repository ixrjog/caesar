package com.baiyi.caesar.factory.engine;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.jenkins.context.BaseJobContext;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/20 9:50 上午
 * @Version 1.0
 */
public interface IJobEngineHandler<T extends BaseJobContext> {

    Integer getKey();

    void trackJobBuildHeartbeat(int buildId);

    BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(int jobId);

    List<CsJobEngine> queryJobEngine(int jobId);

    void trackJobBuild(T buildJobContext);
}
