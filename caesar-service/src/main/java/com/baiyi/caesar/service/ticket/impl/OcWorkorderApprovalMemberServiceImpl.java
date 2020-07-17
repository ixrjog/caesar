package com.baiyi.caesar.service.ticket.impl;

import com.baiyi.caesar.domain.generator.caesar.OcWorkorderApprovalMember;
import com.baiyi.caesar.mapper.caesar.OcWorkorderApprovalMemberMapper;
import com.baiyi.caesar.service.ticket.OcWorkorderApprovalMemberService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:01 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderApprovalMemberServiceImpl implements OcWorkorderApprovalMemberService {

    @Resource
    private OcWorkorderApprovalMemberMapper ocWorkorderApprovalMemberMapper;

    @Override
    public List<OcWorkorderApprovalMember> queryOcWorkorderApprovalMemberByGroupId(int groupId) {
        Example example = new Example(OcWorkorderApprovalMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return ocWorkorderApprovalMemberMapper.selectByExample(example);
    }

    @Override
    public void addOcWorkorderApprovalMember(OcWorkorderApprovalMember ocWorkorderApprovalMember) {
        ocWorkorderApprovalMemberMapper.insert(ocWorkorderApprovalMember);
    }

    @Override
    public void updateOcWorkorderApprovalMember(OcWorkorderApprovalMember ocWorkorderApprovalMember) {
        ocWorkorderApprovalMemberMapper.updateByPrimaryKey(ocWorkorderApprovalMember);
    }

    @Override
    public void deleteOcWorkorderApprovalMemberById(int id) {
        ocWorkorderApprovalMemberMapper.deleteByPrimaryKey(id);
    }
}
