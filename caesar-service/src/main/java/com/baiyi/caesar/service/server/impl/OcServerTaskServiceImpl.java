package com.baiyi.caesar.service.server.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcServerTask;
import com.baiyi.caesar.domain.param.ansible.ServerTaskHistoryParam;
import com.baiyi.caesar.mapper.caesar.OcServerTaskMapper;
import com.baiyi.caesar.service.server.OcServerTaskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:09 下午
 * @Version 1.0
 */
@Service
public class OcServerTaskServiceImpl implements OcServerTaskService {

    @Resource
    private OcServerTaskMapper ocServerTaskMapper;

    @Override
    public DataTable<OcServerTask> queryOcServerTaskByParam(ServerTaskHistoryParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcServerTask> taskList = ocServerTaskMapper.queryOcServerTaskByParam(pageQuery);
        return new DataTable<>(taskList, page.getTotal());
    }

    @Override
    public void addOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskMapper.insert(ocServerTask);
    }

    @Override
    public void updateOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskMapper.updateByPrimaryKey(ocServerTask);
    }

    @Override
    public OcServerTask queryOcServerTaskById(int id) {
        return ocServerTaskMapper.selectByPrimaryKey(id);
    }

}
