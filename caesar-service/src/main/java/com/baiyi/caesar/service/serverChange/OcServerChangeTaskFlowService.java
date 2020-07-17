package com.baiyi.caesar.service.serverChange;

import com.baiyi.caesar.domain.generator.caesar.OcServerChangeTaskFlow;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/28 1:24 下午
 * @Version 1.0
 */
public interface OcServerChangeTaskFlowService {

    List<OcServerChangeTaskFlow> queryOcServerChangeTaskFlowByTaskId(String taskId);

    OcServerChangeTaskFlow queryOcServerChangeTaskFlowById(int id);

    OcServerChangeTaskFlow queryOcServerChangeTaskFlowByParentId(int parentId);

    void addOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow);

    void updateOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow);
}
