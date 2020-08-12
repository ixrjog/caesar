package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobEngine;
import com.baiyi.caesar.mapper.caesar.CsCiJobEngineMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobEngineService;
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
public class CsCiJobEngineServiceImpl implements CsCiJobEngineService {

    @Resource
    private CsCiJobEngineMapper csCiJobEngineMapper;

    @Override
    public void addCsCiJobEngine(CsCiJobEngine csCiJobEngine) {
        csCiJobEngineMapper.insert(csCiJobEngine);
    }

    @Override
    public void updateCsCiJobEngine(CsCiJobEngine csCiJobEngine) {
        csCiJobEngineMapper.updateByPrimaryKey(csCiJobEngine);
    }

    @Override
    public void deleteCsCiJobEngineById(int id) {
        csCiJobEngineMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CsCiJobEngine> queryCsCiJobEngineByJobId(int ciJobId){
        Example example = new Example(CsCiJobEngine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", ciJobId);
        return csCiJobEngineMapper.selectByExample(example);
    }

    @Override
    public  CsCiJobEngine queryCsCiJobEngineById(int id){
        return csCiJobEngineMapper.selectByPrimaryKey(id);
    }

    @Override
    public  CsCiJobEngine queryCsCiJobEngineByUniqueKey(int ciJobId,int jenkinsInstanceId){
        Example example = new Example(CsCiJobEngine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", ciJobId);
        criteria.andEqualTo("jenkinsInstanceId", jenkinsInstanceId);
        return csCiJobEngineMapper.selectOneByExample(example);
    }
}
