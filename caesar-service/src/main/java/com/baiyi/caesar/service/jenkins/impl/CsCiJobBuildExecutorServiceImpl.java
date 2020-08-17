package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildExecutor;
import com.baiyi.caesar.mapper.caesar.CsCiJobBuildExecutorMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildExecutorService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/12 3:21 下午
 * @Version 1.0
 */
@Service
public class CsCiJobBuildExecutorServiceImpl implements CsCiJobBuildExecutorService {

    @Resource
    private CsCiJobBuildExecutorMapper csCiJobBuildExecutorMapper;

    @Override
    public void addCsCiJobBuildExecutor(CsCiJobBuildExecutor csCiJobBuildChange) {
        csCiJobBuildExecutorMapper.insert(csCiJobBuildChange);
    }

    @Override
    public void updateCsCiJobBuildExecutor(CsCiJobBuildExecutor csCiJobBuildChange) {
        csCiJobBuildExecutorMapper.updateByPrimaryKey(csCiJobBuildChange);
    }

    @Override
    public List<CsCiJobBuildExecutor> queryCsCiJobBuildExecutorByBuildId(int buildId) {
        Example example = new Example(CsCiJobBuildExecutor.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId);
        return csCiJobBuildExecutorMapper.selectByExample(example);
    }

    @Override
    public CsCiJobBuildExecutor queryCsCiJobBuildExecutorByUniqueKey(int buildId, String nodeName) {
        Example example = new Example(CsCiJobBuildExecutor.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId);
        criteria.andEqualTo("nodeName", nodeName);
        return csCiJobBuildExecutorMapper.selectOneByExample(example);
    }
}
