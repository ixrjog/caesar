package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CdJobVO;
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

    DataTable<ApplicationVO.Application> queryMyApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery);

    List<ApplicationVO.ScmMember> queryApplicationScmMemberByApplicationId(int applicationId);

    BusinessWrapper<Boolean> addApplication(ApplicationVO.Application application);

    BusinessWrapper<Boolean> updateApplication(ApplicationVO.Application application);

    BusinessWrapper<Boolean> deleteApplicationById(int id);

    BusinessWrapper<Boolean> addApplicationSCMMember(int applicationId, int projectId);

    CsApplicationScmMember queryScmMemberById(int scmMemberId);

    BusinessWrapper<Boolean> removeApplicationSCMMember(int id);

    DataTable<CiJobVO.CiJob> queryCiJobPage(CiJobParam.CiJobPageQuery pageQuery);

    BusinessWrapper<Boolean> addCiJob(CiJobVO.CiJob ciJob);

    BusinessWrapper<Boolean> addCdJob(CdJobVO.CdJob cdJob);

    BusinessWrapper<Boolean> updateCiJob(CiJobVO.CiJob ciJob);

    BusinessWrapper<Boolean> updateCdJob(CdJobVO.CdJob cdJob);

    BusinessWrapper<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(ApplicationParam.ScmMemberBranchQuery scmMemberBranchQuery);

    List<ApplicationVO.Engine> queryApplicationEngineByApplicationId(int applicationId);

    /**
     * 应用引擎配置（engineType决定）
     *
     * @param applicationId
     * @return
     */
    List<ApplicationVO.Engine> acqApplicationEngineByApplicationId(int applicationId);

    BusinessWrapper<Boolean> addApplicationEngine(int applicationId, int jenkinsInstanceId);

    BusinessWrapper<Boolean> removeApplicationEngine(int id);

    BusinessWrapper<Boolean> createCiJobEngine(int ciJobId);

    List<CiJobVO.JobEngine> queryCiJobEngine(int ciJobId);

    BusinessWrapper<Boolean> grantUserApplication(int applicationId, int userId);

    BusinessWrapper<Boolean> revokeUserApplication(int applicationId, int userId);

    BusinessWrapper<Boolean> updateUserApplicationPermission(int applicationId, int userId);
}
