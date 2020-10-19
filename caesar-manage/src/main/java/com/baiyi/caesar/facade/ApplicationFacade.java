package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CdJobParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.application.*;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import org.gitlab.api.models.GitlabBranchCommit;

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

    /**
     * 是否为此应用管理员
     *
     * @param applicationId
     * @param userId
     * @return
     */
    boolean isApplicationAdmin(int applicationId, int userId);

    BusinessWrapper<Boolean> updateApplication(ApplicationVO.Application application);

    /**
     * 修改我的评分
     *
     * @param applicationRate
     * @return
     */
    BusinessWrapper<Boolean> updateMyApplicationRate(ApplicationVO.MyApplicationRate applicationRate);

    BusinessWrapper<Boolean> deleteApplicationById(int id);

    BusinessWrapper<Boolean> addApplicationSCMMember(int applicationId, int projectId);

    CsApplicationScmMember queryScmMemberById(int scmMemberId);

    BusinessWrapper<Boolean> removeApplicationSCMMember(int id);

    DataTable<CiJobVO.CiJob> queryCiJobPage(CiJobParam.CiJobPageQuery pageQuery);

    DataTable<CdJobVO.CdJob> queryCdJobPage(CdJobParam.CdJobPageQuery pageQuery);

    BusinessWrapper<Boolean> addCiJob(CiJobVO.CiJob ciJob);

    BusinessWrapper<Boolean> addCdJob(CdJobVO.CdJob cdJob);

    BusinessWrapper<Boolean> updateCiJob(CiJobVO.CiJob ciJob);

    BusinessWrapper<Boolean> updateCdJob(CdJobVO.CdJob cdJob);

    BusinessWrapper<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(ApplicationParam.ScmMemberBranchQuery query);

    BusinessWrapper<GitlabBranchCommit> queryApplicationSCMMemberBranchCommit(ApplicationParam.ScmMemberBranchCommitQuery query);

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

    BusinessWrapper<Boolean> createJobEngine(int buildType, int jobId);

    List<JobEngineVO.JobEngine> queryJobEngine(int buildType, int jobId);

    BusinessWrapper<Boolean> grantUserApplication(int applicationId, int userId);

    BusinessWrapper<Boolean> revokeUserApplication(int applicationId, int userId);

    BusinessWrapper<Boolean> grantUserApplicationBuildJob(int ciJobId, int userId);

    BusinessWrapper<Boolean> revokeUserApplicationBuildJob(int ciJobId, int userId);

    BusinessWrapper<Boolean> updateUserApplicationPermission(int applicationId, int userId);

    DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ApplicationParam.ServerGroupPageQuery pageQuery);

    List<ApplicationServerGroupVO.ApplicationServerGroup> queryApplicationServerGroupByApplicationId(int applicationId);

    BusinessWrapper<Boolean> addApplicationServerGroup(ApplicationServerGroupVO.ApplicationServerGroup applicationServerGroup);

    BusinessWrapper<Boolean> removeApplicationServerGroup(int id);
}
