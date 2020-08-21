package com.baiyi.caesar.task;

import com.baiyi.caesar.facade.jenkins.JobFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/21 1:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JobBuildTask extends BaseTask {

    @Resource
    private JobFacade jobFacade;

    // Terminal
    public static final String TASK_JOB_BUILD_KEY = "TASK_JOB_BUILD_KEY";

    private static final int LOCK_MINUTE = 1;

    /**
     * 追踪构建任务
     */
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    public void trackJobBuildTask() {
        if (tryLock()) return;
        jobFacade.trackJobBuildTask();
        unlock();
    }

    @Override
    protected String getLock() {
        return TASK_JOB_BUILD_KEY;
    }

    @Override
    protected String getTaskName() {
        return "Jenkins构建任务追踪";
    }

    @Override
    protected int getLockMinute() {
        return LOCK_MINUTE;
    }

}
