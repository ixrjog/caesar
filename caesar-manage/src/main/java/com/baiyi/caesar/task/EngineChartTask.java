package com.baiyi.caesar.task;

import com.baiyi.caesar.facade.jenkins.JenkinsEngineFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/3/18 2:35 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class EngineChartTask extends BaseTask {

    @Resource
    private JenkinsEngineFacade jenkinsEngineFacade;

    /**
     * 追踪构建任务
     */
    @Scheduled(initialDelay = 3000, fixedRate = 5 * 1000)
    public void engineChartTask() {
        //if (!isHealth()) return;
        //log.info("任务启动: 构建引擎视图！");
        jenkinsEngineFacade.buildEngineChart();
    }
}