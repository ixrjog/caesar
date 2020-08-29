package com.baiyi.caesar.factory.jenkins.engine;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.jenkins.context.JobBuildContext;
import com.baiyi.caesar.jenkins.context.JobDeploymentContext;

/**
 * @Author baiyi
 * @Date 2020/8/5 10:30 上午
 * @Version 1.0
 */

public interface JenkinsJobEngineHandler {

    /**
     * 随机取一个可用的任务工作引擎
     * @param csJob
     * @return
     */
    BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCiJob csJob);

    BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCdJob csJob);

    JobEngineVO.JobEngine acqJobEngineByJobEngineId(int jobEngineId);

    void trackJobBuild(JobBuildContext context);

    void trackJobBuild(JobDeploymentContext context);

    void trackJobBuildHeartbeat(int buildType,int buildId);


}
