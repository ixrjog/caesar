package com.baiyi.caesar.service.serverChange;

import com.baiyi.caesar.domain.generator.caesar.OcServerChangeTask;

/**
 * @Author baiyi
 * @Date 2020/5/27 1:47 下午
 * @Version 1.0
 */
public interface OcServerChangeTaskService {

    void addOcServerChangeTask(OcServerChangeTask ocServerChangeTask);

    void updateOcServerChangeTask(OcServerChangeTask ocServerChangeTask);

    OcServerChangeTask checkOcServerChangeTask(OcServerChangeTask ocServerChangeTask);

    OcServerChangeTask queryOcServerChangeTaskByTaskId(String taskId);
}
