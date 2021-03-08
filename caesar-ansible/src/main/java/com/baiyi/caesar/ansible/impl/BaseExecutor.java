package com.baiyi.caesar.ansible.impl;

import com.baiyi.caesar.ansible.IAnsibleExecutor;
import com.baiyi.caesar.ansible.config.AnsibleConfig;
import com.baiyi.caesar.ansible.factory.ExecutorFactory;
import com.baiyi.caesar.ansible.handler.AnsibleTaskHandler;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.RedisKeyUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.service.server.OcServerTaskService;
import com.baiyi.caesar.service.user.OcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/17 8:55 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseExecutor implements InitializingBean, IAnsibleExecutor {

    @Resource
    protected AnsibleConfig ansibleConfig;

    @Resource
    private OcUserService ocUserService;

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    protected OcServerTaskService ocServerTaskService;

    @Resource
    protected AnsibleTaskHandler ansibleTaskHandler;

    protected OcUser getOcUser() {
        return ocUserService.queryOcUserByUsername(SessionUtil.getUsername());
    }

    protected void addOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskService.addOcServerTask(ocServerTask);
    }

    protected BusinessWrapper<OcServerTask> getResultWrapper(OcServerTask ocServerTask) {
        return new BusinessWrapper(ocServerTask);
    }

    protected BusinessWrapper<Map<String, String>> getServerTreeHostPatternMap(String uuid, OcUser ocUser) {
        String key = RedisKeyUtil.getMyServerTreeKey(ocUser.getId(), uuid);
        if (!redisUtil.hasKey(key))
            return new BusinessWrapper<>(ErrorEnum.SERVER_TASK_TREE_NOT_EXIST);
        Map<String, String> serverTreeHostPatternMap = (Map<String, String>) redisUtil.get(key);
        return new BusinessWrapper(serverTreeHostPatternMap);
    }

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    @Override
    public BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, List<ServerVO.Server> servers) {
        return new BusinessWrapper();
    }

    @Override
    public BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, OcServer ocServer) {
        return new BusinessWrapper();
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ExecutorFactory.register(this);
    }

}
