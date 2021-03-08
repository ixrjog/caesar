package com.baiyi.caesar.ansible.impl;

import com.baiyi.caesar.ansible.IAnsibleExecutor;
import com.baiyi.caesar.ansible.builder.ServerTaskBuilder;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.facade.ServerBaseFacade;
import com.baiyi.caesar.service.ansible.OcAnsiblePlaybookService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 9:54 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsiblePlaybookExecutor extends BaseExecutor implements IAnsibleExecutor {

    public static final String COMPONENT_NAME = "AnsiblePlaybookExecutor";

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    @Override
    public BusinessWrapper<OcServerTask> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskPlaybookExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor = (ServerTaskExecutorParam.ServerTaskPlaybookExecutor) taskExecutor;
        OcUser ocUser = getOcUser();

        BusinessWrapper<Map<String, String>> wrapper = getServerTreeHostPatternMap(serverTaskPlaybookExecutor.getUuid(), ocUser);
        if (!wrapper.isSuccess())
            return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        Map<String, String> serverTreeHostPatternMap = wrapper.getBody();

        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(ocUser, serverTreeHostPatternMap, serverTaskPlaybookExecutor);
        addOcServerTask(ocServerTask);
        // 重新写入playbook
        OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(serverTaskPlaybookExecutor.getPlaybookId());
        String playbookPath = ansibleConfig.getPlaybookPath(ocAnsiblePlaybook);
        IOUtil.writeFile(ocAnsiblePlaybook.getPlaybook(), playbookPath);

        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskPlaybookExecutor, playbookPath);
        return getResultWrapper(ocServerTask);
    }

    @Override
    public BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, List<ServerVO.Server> servers) {
        if (!(taskExecutor instanceof ServerTaskExecutorParam.ServerTaskPlaybookExecutor))
            return new BusinessWrapper(ErrorEnum.EXECUTOR_PARAM_TYPE_ERROR);
        ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor = (ServerTaskExecutorParam.ServerTaskPlaybookExecutor) taskExecutor;
        Map<String, String> serverTreeHostPatternMap = Maps.newHashMap();
        servers.forEach(e-> serverTreeHostPatternMap.put(ServerBaseFacade.acqServerName(e), e.getPrivateIp()));
        // 录入任务
        OcServerTask ocServerTask = ServerTaskBuilder.build(null, serverTreeHostPatternMap, serverTaskPlaybookExecutor);
        return executor(ocServerTask, serverTaskPlaybookExecutor);
    }


    private BusinessWrapper<OcServerTask> executor(OcServerTask ocServerTask, ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor) {
        addOcServerTask(ocServerTask);
        // 重新写入脚本
        OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(serverTaskPlaybookExecutor.getPlaybookId());
        String playbookPath = ansibleConfig.getPlaybookPath(ocAnsiblePlaybook);
        IOUtil.writeFile(ocAnsiblePlaybook.getPlaybook(), playbookPath);
        // 异步执行
        ansibleTaskHandler.call(ocServerTask, serverTaskPlaybookExecutor, playbookPath);
        return getResultWrapper(ocServerTask);
    }


}
