package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildExecutor;
import com.baiyi.caesar.mapper.caesar.CsJobBuildExecutorMapper;
import com.baiyi.caesar.service.jenkins.CsJobBuildExecutorService;
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
public class CsJobBuildExecutorServiceImpl implements CsJobBuildExecutorService {

    @Resource
    private CsJobBuildExecutorMapper csJobBuildExecutorMapper;

    @Override
    public void addCsJobBuildExecutor(CsJobBuildExecutor csJobBuildExecutor) {
        csJobBuildExecutorMapper.insert(csJobBuildExecutor);
    }

    @Override
    public void updateCsJobBuildExecutor(CsJobBuildExecutor csJobBuildExecutor) {
        csJobBuildExecutorMapper.updateByPrimaryKey(csJobBuildExecutor);
    }

    @Override
    public List<CsJobBuildExecutor> queryCsJobBuildExecutorByBuildId(int buildType, int buildId) {
        Example example = new Example(CsJobBuildExecutor.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        return csJobBuildExecutorMapper.selectByExample(example);
    }

    @Override
    public CsJobBuildExecutor queryCsJobBuildExecutorByUniqueKey(int buildType, int buildId, String nodeName) {
        Example example = new Example(CsJobBuildExecutor.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        criteria.andEqualTo("nodeName", nodeName);
        return csJobBuildExecutorMapper.selectOneByExample(example);
    }

    @Override
    public void deleteCsJobBuildExecutorById(int id) {
        csJobBuildExecutorMapper.deleteByPrimaryKey(id);
    }
}
