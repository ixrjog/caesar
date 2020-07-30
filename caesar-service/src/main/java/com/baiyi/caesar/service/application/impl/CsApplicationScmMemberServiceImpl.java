package com.baiyi.caesar.service.application.impl;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.mapper.caesar.CsApplicationScmMemberMapper;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/21 5:41 下午
 * @Version 1.0
 */
@Service
public class CsApplicationScmMemberServiceImpl implements CsApplicationScmMemberService {

    @Resource
    private CsApplicationScmMemberMapper csApplicationScmMemberMapper;

    @Override
    public List<CsApplicationScmMember> queryCsApplicationScmMemberByApplicationId(int applicationId) {
        Example example = new Example(CsApplicationScmMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return csApplicationScmMemberMapper.selectByExample(example);
    }

    @Override
    public CsApplicationScmMember queryCsApplicationScmMemberByUniqueKey(int applicationId, int scmId) {
        Example example = new Example(CsApplicationScmMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        criteria.andEqualTo("scmId", scmId);
        return csApplicationScmMemberMapper.selectOneByExample(example);
    }

    @Override
    public CsApplicationScmMember queryCsApplicationScmMemberById(int id) {
        return csApplicationScmMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsApplicationScmMember(CsApplicationScmMember csApplicationScmMember) {
        csApplicationScmMemberMapper.insert(csApplicationScmMember);
    }

    @Override
    public void updateCsApplicationScmMember(CsApplicationScmMember csApplicationScmMember) {
        csApplicationScmMemberMapper.updateByPrimaryKey(csApplicationScmMember);
    }

    @Override
    public void deleteCsApplicationScmMemberById(int id) {
        csApplicationScmMemberMapper.deleteByPrimaryKey(id);
    }

}
