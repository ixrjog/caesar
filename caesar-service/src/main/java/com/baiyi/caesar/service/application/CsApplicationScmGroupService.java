package com.baiyi.caesar.service.application;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmGroup;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/6 2:18 下午
 * @Version 1.0
 */
public interface CsApplicationScmGroupService {

    List<CsApplicationScmGroup> queryApplicationScmGroupByApplicationId(int applicationId);

    List<CsApplicationScmGroup> queryApplicationScmGroupByGroupId(int groupId);
}
