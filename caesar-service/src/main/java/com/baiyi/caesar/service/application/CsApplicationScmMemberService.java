package com.baiyi.caesar.service.application;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/21 5:41 下午
 * @Version 1.0
 */
public interface CsApplicationScmMemberService {

    List<CsApplicationScmMember> queryCsApplicationScmMemberByApplicationId(int applicationId);

    CsApplicationScmMember queryCsApplicationScmMemberByUniqueKey(int applicationId,int scmId);

    CsApplicationScmMember queryCsApplicationScmMemberById(int id);

    List<CsApplicationScmMember> queryCsApplicationScmMemberByScmId(int scmId);

    void addCsApplicationScmMember(CsApplicationScmMember csApplicationScmMember);

    void updateCsApplicationScmMember(CsApplicationScmMember csApplicationScmMember);

    void deleteCsApplicationScmMemberById(int id);
}
