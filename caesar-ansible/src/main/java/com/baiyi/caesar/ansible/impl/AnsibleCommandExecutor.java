package com.baiyi.caesar.ansible.impl;

import com.baiyi.caesar.ansible.IAnsibleExecutor;
import com.baiyi.caesar.ansible.builder.ServerTaskBuilder;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 9:04 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleCommandExecutor extends BaseExecutor implements IAnsibleExecutor {

    public static final String COMPONENT_NAME = "AnsibleCommandExecutor";

    @Override
    public BusinessWrapper<OcServerTask> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskCommandExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor = (ServerTaskExecutorParam.ServerTaskCommandExecutor) taskExecutor;
        OcUser ocUser = getOcUser();
        BusinessWrapper<Map<String, String>> wrapper = getServerTreeHostPatternMap(serverTaskCommandExecutor.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        Map<String, String> serverTreeHostPatternMap = wrapper.getBody();

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskCommandExecutor);
        addOcServerTask(ocServerTask);
        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskCommandExecutor);
        return getResultWrapper(ocServerTask);
    }

    @Override
    public BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, OcServer ocServer) {
        return new BusinessWrapper<>();
    }

}
