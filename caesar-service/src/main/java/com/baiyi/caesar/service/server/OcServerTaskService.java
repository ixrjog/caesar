package com.baiyi.caesar.service.server;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.param.ansible.ServerTaskHistoryParam;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:09 下午
 * @Version 1.0
 */
public interface OcServerTaskService {

    DataTable<OcServerTask> queryOcServerTaskByParam(ServerTaskHistoryParam.PageQuery pageQuery);

    void addOcServerTask(OcServerTask ocServerTask);

    void updateOcServerTask(OcServerTask ocServerTask);

    OcServerTask queryOcServerTaskById(int id);
}
