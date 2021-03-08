package com.baiyi.caesar.ansible.impl;

import com.baiyi.caesar.ansible.IAnsibleExecutor;
import com.baiyi.caesar.ansible.builder.ServerTaskBuilder;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.OcAnsibleScript;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.caesar.facade.ServerBaseFacade;
import com.baiyi.caesar.service.ansible.OcAnsibleScriptService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 9:37 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleScriptExecutor extends BaseExecutor implements IAnsibleExecutor {

    public static final String COMPONENT_NAME = "AnsibleScriptExecutor";

    @Resource
    private OcAnsibleScriptService ocAnsibleScriptService;

    @Override
    public BusinessWrapper<OcServerTask> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskScriptExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor = (ServerTaskExecutorParam.ServerTaskScriptExecutor) taskExecutor;
        OcUser ocUser = getOcUser();

        BusinessWrapper<Map<String, String>> wrapper = getServerTreeHostPatternMap(serverTaskScriptExecutor.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return new BusinessWrapper(wrapper.getCode(), wrapper.getDesc());
        Map<String, String> serverTreeHostPatternMap = wrapper.getBody();

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskScriptExecutor);
        return executor(ocServerTask, serverTaskScriptExecutor);
    }

    @Override
    public BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, OcServer ocServer) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskScriptExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor = (ServerTaskExecutorParam.ServerTaskScriptExecutor) taskExecutor;

        Map<String, String> serverTreeHostPatternMap = Maps.newHashMap();
        serverTreeHostPatternMap.put(ServerBaseFacade.acqServerName(ocServer), ocServer.getPrivateIp());

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(null, serverTreeHostPatternMap, serverTaskScriptExecutor);

        return executor(ocServerTask, serverTaskScriptExecutor);
    }

    private BusinessWrapper<OcServerTask> executor(OcServerTask ocServerTask, ServerTaskExecutorParam.ServerTaskScriptExecutor serverTaskScriptExecutor) {
        addOcServerTask(ocServerTask);
        // 重新写入脚本
        OcAnsibleScript ocAnsibleScript = ocAnsibleScriptService.queryOcAnsibleScriptById(serverTaskScriptExecutor.getScriptId());
        String scriptPath = ansibleConfig.getScriptPath(ocAnsibleScript);
        IOUtil.writeFile(ocAnsibleScript.getScriptContent(), scriptPath);

        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskScriptExecutor, scriptPath);
        return getResultWrapper(ocServerTask);
    }

}
