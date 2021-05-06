package com.baiyi.caesar.service.application.impl;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationResource;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.mapper.caesar.CsApplicationResourceMapper;
import com.baiyi.caesar.service.application.CsApplicationResourceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/6 1:27 下午
 * @Version 1.0
 */
@Service
public class CsApplicationResourceServiceImpl implements CsApplicationResourceService {

    @Resource
    private CsApplicationResourceMapper csApplicationResourceMapper;

    @Override
    public void addApplicationResource(CsApplicationResource csApplicationResource) {
        csApplicationResourceMapper.insert(csApplicationResource);
    }

    @Override
    public void updateApplicationResource(CsApplicationResource csApplicationResource) {
        csApplicationResourceMapper.updateByPrimaryKey(csApplicationResource);
    }

    @Override
    public CsApplicationResource queryApplicationResourceByUniqueKey(int applicationId, String resType ,String resKey) {
        Example example = new Example(CsApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        criteria.andEqualTo("resType", resType);
        criteria.andEqualTo("resKey", resKey);
        return csApplicationResourceMapper.selectOneByExample(example);
    }
}
