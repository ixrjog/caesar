package com.baiyi.caesar.task;

import com.baiyi.caesar.facade.jenkins.JobFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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

    /**
     * 追踪构建任务
     */
    @Scheduled(initialDelay = 5000, fixedRate = 29 * 1000)
    @SchedulerLock(name = "trackJobBuildTask", lockAtMostFor = "30m", lockAtLeastFor = "1m")
    public void trackJobBuildTask() {
        if(!isHealth()) return;
        log.info("任务启动: 持续集成构建任务追踪！");
        jobFacade.trackJobBuildTask();
    }


}
