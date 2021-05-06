package com.baiyi.caesar.service.application.impl;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationServerGroup;
import com.baiyi.caesar.mapper.caesar.CsApplicationServerGroupMapper;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/10 9:58 上午
 * @Version 1.0
 */
@Service
public class CsApplicationServerGroupServiceImpl implements CsApplicationServerGroupService {

    @Resource
    private CsApplicationServerGroupMapper csApplicationServerGroupMapper;

    @Override
    public List<CsApplicationServerGroup> selectAll() {
        return csApplicationServerGroupMapper.selectAll();
    }

    @Override
    public List<CsApplicationServerGroup> queryCsApplicationServerGroupByApplicationId(int applicationId) {
        Example example = new Example(CsApplicationServerGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return csApplicationServerGroupMapper.selectByExample(example);
    }

    @Override
    public CsApplicationServerGroup queryCsApplicationServerGroupByUniqueKey(CsApplicationServerGroup csApplicationServerGroup) {
        Example example = new Example(CsApplicationServerGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", csApplicationServerGroup.getApplicationId());
        criteria.andEqualTo("source", csApplicationServerGroup.getSource());
        criteria.andEqualTo("serverGroupId", csApplicationServerGroup.getServerGroupId());
        return csApplicationServerGroupMapper.selectOneByExample(example);
    }

    @Override
    public void addCsApplicationServerGroup(CsApplicationServerGroup csApplicationServerGroup) {
        csApplicationServerGroupMapper.insert(csApplicationServerGroup);
    }

    @Override
    public void updateCsApplicationServerGroup(CsApplicationServerGroup csApplicationServerGroup) {
        csApplicationServerGroupMapper.updateByPrimaryKey(csApplicationServerGroup);
    }

    @Override
    public void deleteCsApplicationServerGroupById(int id) {
        csApplicationServerGroupMapper.deleteByPrimaryKey(id);
    }
}
