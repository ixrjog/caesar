package com.baiyi.caesar.ansible;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.caesar.domain.vo.server.ServerVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/17 8:57 上午
 * @Version 1.0
 */
public interface IAnsibleExecutor {

    String getKey();

    /**
     * 外部调用
     * @param taskExecutor
     * @return
     */
    BusinessWrapper<OcServerTask> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor);

    /**
     * 内部接口(不鉴权)
     * @param taskExecutor
     * @param ocServer
     * @return
     */
    BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, OcServer ocServer);

    /**
     *  内部接口(不鉴权)
     * @param taskExecutor
     * @return
     */
    BusinessWrapper<OcServerTask> executor(ServerTaskExecutorParam.TaskExecutor taskExecutor, List<ServerVO.Server> servers);

}
