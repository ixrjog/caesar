package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.param.ansible.ServerTaskHistoryParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerTaskMapper extends Mapper<OcServerTask> {

    List<OcServerTask> queryOcServerTaskByParam(ServerTaskHistoryParam.PageQuery pageQuery);
}