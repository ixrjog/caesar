package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.mapper.caesar.CsJobEngineMapper;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/3 4:17 下午
 * @Version 1.0
 */
@Service
public class CsJobEngineServiceImpl implements CsJobEngineService {

    @Resource
    private CsJobEngineMapper csJobEngineMapper;

    @Override
    public void addCsJobEngine(CsJobEngine csJobEngine) {
        csJobEngineMapper.insert(csJobEngine);
    }

    @Override
    public void updateCsJobEngine(CsJobEngine csJobEngine) {
        csJobEngineMapper.updateByPrimaryKey(csJobEngine);
    }

    @Override
    public void deleteCsJobEngineById(int id) {
        csJobEngineMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CsJobEngine> queryCsJobEngineByJobId(int buildType, int jobId) {
        Example example = new Example(CsJobEngine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("jobId", jobId);
        return csJobEngineMapper.selectByExample(example);
    }

    @Override
    public CsJobEngine queryCsJobEngineById(int id) {
        return csJobEngineMapper.selectByPrimaryKey(id);
    }

    @Override
    public CsJobEngine queryCsJobEngineByUniqueKey(int buildType,int jobId, int jenkinsInstanceId) {
        Example example = new Example(CsJobEngine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("jobId", jobId);
        criteria.andEqualTo("jenkinsInstanceId", jenkinsInstanceId);
        //criteria.andEqualTo("name", name);
        return csJobEngineMapper.selectOneByExample(example);
    }
}
