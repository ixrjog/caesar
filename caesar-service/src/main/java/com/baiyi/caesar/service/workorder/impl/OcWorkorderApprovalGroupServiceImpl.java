package com.baiyi.caesar.service.workorder.impl;

import com.baiyi.caesar.domain.generator.caesar.OcWorkorderApprovalGroup;
import com.baiyi.caesar.mapper.caesar.OcWorkorderApprovalGroupMapper;
import com.baiyi.caesar.service.workorder.OcWorkorderApprovalGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/6 2:33 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderApprovalGroupServiceImpl implements OcWorkorderApprovalGroupService {

    @Resource
    private OcWorkorderApprovalGroupMapper ocWorkorderApprovalGroupMapper;

    @Override
    public OcWorkorderApprovalGroup queryOcWorkorderApprovalGroupById(int id) {
        return ocWorkorderApprovalGroupMapper.selectByPrimaryKey(id);

    }

}
