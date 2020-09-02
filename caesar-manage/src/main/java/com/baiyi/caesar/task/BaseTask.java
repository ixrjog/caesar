package com.baiyi.caesar.task;

import com.baiyi.caesar.config.CaesarConfig;
import com.baiyi.caesar.task.util.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/8 1:37 下午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseTask {

    @Resource
    protected TaskUtil taskUtil;

    @Resource
    private CaesarConfig opscloudConfig;

    private static final int LOCK_MINUTE = 5;

    /**
     * 尝试加锁
     *
     * @return
     */
    protected boolean tryLock() {
        if (!opscloudConfig.getOpenTask()) return true;
        if (taskUtil.tryLock(getLock())) return true;
        taskUtil.lock(getLock(), getLockMinute());
        log.info("{} : 开始执行!", getTaskName());
        return false;
    }

    protected void unlock() {
        taskUtil.unlock(getLock());
        log.info("{} : 执行结束!", getTaskName());
    }

    abstract String getLock();

    abstract String getTaskName();

    protected int getLockMinute() {
        return LOCK_MINUTE;
    }
}
