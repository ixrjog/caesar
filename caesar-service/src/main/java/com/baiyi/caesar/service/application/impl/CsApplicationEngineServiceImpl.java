package com.baiyi.caesar.service.application.impl;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationEngine;
import com.baiyi.caesar.mapper.caesar.CsApplicationEngineMapper;
import com.baiyi.caesar.service.application.CsApplicationEngineService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/3 12:54 下午
 * @Version 1.0
 */
@Service
public class CsApplicationEngineServiceImpl implements CsApplicationEngineService {

    @Resource
    private CsApplicationEngineMapper csApplicationEngineMapper;

    @Override
    public CsApplicationEngine queryCsApplicationEngineByUniqueKey(int applicationId, int jenkinsInstanceId){
        Example example = new Example(CsApplicationEngine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        criteria.andEqualTo("jenkinsInstanceId",jenkinsInstanceId);
        return csApplicationEngineMapper.selectOneByExample(example);
    }

    @Override
    public List<CsApplicationEngine> queryCsApplicationEngineByApplicationId(int applicationId) {
        Example example = new Example(CsApplicationEngine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return csApplicationEngineMapper.selectByExample(example);
    }

    @Override
    public List<CsApplicationEngine> selectAll(){
        return csApplicationEngineMapper.selectAll();
    }

    @Override
    public void addCsApplicationEngine(CsApplicationEngine csApplicationEngine) {
        csApplicationEngineMapper.insert(csApplicationEngine);
    }

    @Override
    public void updateCsApplicationEngine(CsApplicationEngine csApplicationEngine) {
        csApplicationEngineMapper.updateByPrimaryKey(csApplicationEngine);
    }

    @Override
    public void deleteCsApplicationById(int id) {
        csApplicationEngineMapper.deleteByPrimaryKey(id);
    }
}
