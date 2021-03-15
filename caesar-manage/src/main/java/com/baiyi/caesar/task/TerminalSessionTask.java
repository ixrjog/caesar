package com.baiyi.caesar.task;

import com.baiyi.caesar.facade.TerminalFacade;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/26 6:01 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TerminalSessionTask extends BaseTask{

    @Resource
    private TerminalFacade terminalFacade;

    /**
     * 关闭无效会话
     */
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    /**
     * By setting lockAtMostFor we make sure that the lock is released even if the node dies and by setting
     * `lockAtLeastFor` we make sure it's not executed more than once in fifteen minutes. Please note that
     * `lockAtMostFor` is just a safety net in case that the node executing the task dies, so set it to a time
     * that is significantly larger than maximum estimated execution time. If the task takes longer than lockAtMostFor,
     * it may be executed again and the results will be unpredictable (more processes will hold the lock).
     */
    @SchedulerLock(name = "closeInvalidSessionTask", lockAtMostFor = "2m", lockAtLeastFor = "2m")
    public void closeInvalidSessionTask() {
        if (!isHealth()) return;
        log.info("任务启动: 关闭无效的终端会话！");
        terminalFacade.closeInvalidSessionTask();
    }


}
