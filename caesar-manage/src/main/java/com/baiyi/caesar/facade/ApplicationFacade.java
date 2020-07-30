package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/21 2:49 下午
 * @Version 1.0
 */
public interface ApplicationFacade {

    DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery);

    List<ApplicationVO.ScmMember> queryApplicationScmMemberByApplicationId(int applicationId);

    BusinessWrapper<Boolean> addApplication(ApplicationVO.Application application);

    BusinessWrapper<Boolean> updateApplication(ApplicationVO.Application application);

    BusinessWrapper<Boolean> deleteApplicationById(int id);

    BusinessWrapper<Boolean> addApplicationSCMMember(int applicationId, int projectId);

    BusinessWrapper<Boolean> removeApplicationSCMMember(int id);

    DataTable<CiJobVO.CiJob> queryCiJobPage(CiJobParam.CiJobPageQuery pageQuery);

    BusinessWrapper<Boolean> addCiJob(CiJobVO.CiJob ciJob);

    BusinessWrapper<Boolean> updateCiJob(CiJobVO.CiJob ciJob);

    BusinessWrapper<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(ApplicationParam.ScmMemberBranchQuery scmMemberBranchQuery);
}
