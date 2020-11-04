package com.baiyi.caesar.factory.jenkins.engine;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/5 10:30 上午
 * @Version 1.0
 */

public interface JobEngineHandler {

    /**
     * 随机取一个可用的任务工作引擎
     *
     * @param csJob
     * @return
     */
    BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCiJob csJob);

    BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(CsCdJob csJob);

    JobEngineVO.JobEngine acqJobEngineByJobEngineId(int jobEngineId);

    boolean tryJenkinsInstanceActive(int jenkinsInstanceId);

    List<CsJobEngine> queryJobEngine(int buildType, int jobId);

    void trackJobBuild(BuildJobContext context);

    void trackJobBuild(DeploymentJobContext context);

    void trackJobBuildHeartbeat(int buildType, int buildId);


}
