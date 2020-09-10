package com.baiyi.caesar.service.application;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationServerGroup;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/10 9:58 上午
 * @Version 1.0
 */
public interface CsApplicationServerGroupService {

    List<CsApplicationServerGroup> queryCsApplicationServerGroupByApplicationId(int applicationId);

    CsApplicationServerGroup queryCsApplicationServerGroupByUniqueKey(CsApplicationServerGroup csApplicationServerGroup);

    void addCsApplicationServerGroup(CsApplicationServerGroup csApplicationServerGroup);

    void updateCsApplicationServerGroup(CsApplicationServerGroup csApplicationServerGroup);

    void deleteCsApplicationServerGroupById(int id);
}
