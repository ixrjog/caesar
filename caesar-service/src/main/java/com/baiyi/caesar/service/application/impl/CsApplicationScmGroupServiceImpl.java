package com.baiyi.caesar.service.application.impl;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmGroup;
import com.baiyi.caesar.mapper.caesar.CsApplicationScmGroupMapper;
import com.baiyi.caesar.service.application.CsApplicationScmGroupService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/6 2:18 下午
 * @Version 1.0
 */
@Service
public class CsApplicationScmGroupServiceImpl implements CsApplicationScmGroupService {

    @Resource
    private CsApplicationScmGroupMapper csApplicationScmGroupMapper;

    @Override
    public List<CsApplicationScmGroup> queryApplicationScmGroupByApplicationId(int applicationId) {
        Example example = new Example(CsApplicationScmGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return csApplicationScmGroupMapper.selectByExample(example);
    }

    @Override
    public List<CsApplicationScmGroup> queryApplicationScmGroupByGroupId(int groupId) {
        Example example = new Example(CsApplicationScmGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return csApplicationScmGroupMapper.selectByExample(example);
    }
}
