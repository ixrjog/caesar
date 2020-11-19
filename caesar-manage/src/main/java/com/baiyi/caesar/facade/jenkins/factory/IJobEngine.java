package com.baiyi.caesar.facade.jenkins.factory;

import com.baiyi.caesar.domain.BusinessWrapper;

/**
 * @Author baiyi
 * @Date 2020/11/18 11:29 上午
 * @Version 1.0
 */
public interface IJobEngine {

    BusinessWrapper<Boolean> correctionJobEngine(int jobId);

    /**
     * 创建任务引擎
     * @param jobId
     */
    void createJobEngine(int jobId);

    /**
     * 更新任务引擎（更换模版）
     *
     * @param jobId
     */
    void updateJobEngine(int jobId);

    Integer getKey();
}
