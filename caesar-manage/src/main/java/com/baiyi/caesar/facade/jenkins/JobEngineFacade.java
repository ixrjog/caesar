package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.vo.application.JobEngineVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/3 4:33 下午
 * @Version 1.0
 */
public interface JobEngineFacade {


    /**
     * 创建Job引擎配置
     *
     * @param jobId
     */
    void createJobEngine(int buildType, int jobId);

    /**
     * 更新Job引擎配置
     * @param buildType
     * @param jobId
     */
    void updateJobEngine(int buildType, int jobId);

    boolean deleteJobBuildEngine(int jobId);

    boolean deleteJobDeploymentEngine(int jobId);



    List<JobEngineVO.JobEngine> queryJobEngines(int buildType, int jobId);

    String acqJenkinsServerName(Integer jobEngineId);
}
