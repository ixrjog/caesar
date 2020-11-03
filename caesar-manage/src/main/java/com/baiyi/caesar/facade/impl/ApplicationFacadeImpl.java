package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.bo.UserPermissionBO;
import com.baiyi.caesar.builder.ApplicationScmMemberBuilder;
import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.RegexUtils;
import com.baiyi.caesar.decorator.application.*;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CdJobParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.param.user.UserBusinessGroupParam;
import com.baiyi.caesar.domain.vo.application.*;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.facade.*;
import com.baiyi.caesar.facade.jenkins.JenkinsJobFacade;
import com.baiyi.caesar.opscloud.OpscloudServer;
import com.baiyi.caesar.service.application.CsApplicationEngineService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.user.OcUserGroupService;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/21 2:49 下午
 * @Version 1.0
 */
@Service
public class ApplicationFacadeImpl implements ApplicationFacade {

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsApplicationEngineService csApplicationEngineService;

    @Resource
    private ApplicationDecorator applicationDecorator;

    @Resource
    private ApplicationScmMemberDecorator applicationScmMemberDecorator;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CiJobDecorator ciJobDecorator;

    @Resource
    private CdJobDecorator cdJobDecorator;

    @Resource
    private ApplicationEngineDecorator applicationEngineDecorator;

    @Resource
    private JenkinsJobFacade jenkinsCiJobFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private UserFacade userFacade;

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Resource
    private OpscloudServer opscloudServer;

    @Resource
    private CsApplicationServerGroupService csApplicationServerGroupService;

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<CsApplication> table = csApplicationService.queryCsApplicationByParam(pageQuery);
        List<ApplicationVO.Application> page = BeanCopierUtils.copyListProperties(table.getData(), ApplicationVO.Application.class);
        return new DataTable<>(page.stream().map(e -> applicationDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<ApplicationVO.Application> queryMyApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        ApplicationParam.MyApplicationPageQuery myApplicationPageQuery = BeanCopierUtils.copyProperties(pageQuery, ApplicationParam.MyApplicationPageQuery.class);
        OcUser ocUser = userFacade.getOcUserBySession();
        myApplicationPageQuery.setUserId(ocUser.getId());
        DataTable<CsApplication> table = csApplicationService.queryMyCsApplicationByParam(myApplicationPageQuery);
        List<ApplicationVO.Application> page = BeanCopierUtils.copyListProperties(table.getData(), ApplicationVO.Application.class);
        return new DataTable<>(page.stream().map(e -> applicationDecorator.decorator(e, ocUser, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public List<ApplicationVO.ScmMember> queryApplicationScmMemberByApplicationId(int applicationId) {
        return csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(applicationId)
                .stream().map(e -> applicationScmMemberDecorator.decorator(BeanCopierUtils.copyProperties(e, ApplicationVO.ScmMember.class), 1)).collect(Collectors.toList());
    }

    @Override
    public BusinessWrapper<Boolean> addApplication(ApplicationVO.Application application) {
        if (!RegexUtils.isApplicationKeyRule(application.getApplicationKey()))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_KEY_NON_COMPLIANCE_WITH_RULES);
        CsApplication csApplication = BeanCopierUtils.copyProperties(application, CsApplication.class);
        csApplicationService.addCsApplication(csApplication);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateApplication(ApplicationVO.Application application) {
        CsApplication pre = BeanCopierUtils.copyProperties(application, CsApplication.class);
        CsApplication csApplication = csApplicationService.queryCsApplicationById(pre.getId());
        pre.setApplicationKey(csApplication.getApplicationKey());
        csApplicationService.updateCsApplication(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public boolean isApplicationAdmin(int applicationId, int userId) {
        List<OcUserPermission> userPermissions = userPermissionFacade.queryUserBusinessPermissionByUserId(userId, BusinessType.APPLICATION.getType());
        if (CollectionUtils.isEmpty(userPermissions))
            return false;
        return userPermissions.stream().anyMatch(e -> "ADMIN".equals(e.getRoleName()));
    }

    @Override
    public BusinessWrapper<Boolean> updateMyApplicationRate(ApplicationVO.MyApplicationRate applicationRate) {
        OcUser ocUser = userFacade.getOcUserBySession();
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionById(applicationRate.getUserPermissionId());
        if (ocUserPermission.getUserId().equals(ocUser.getId()) && ocUserPermission.getBusinessType() == BusinessType.APPLICATION.getType()) {
            ocUserPermission.setRate(applicationRate.getRate());
            ocUserPermissionService.updateOcUserPermission(ocUserPermission);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        }
    }

    @Override
    public BusinessWrapper<Boolean> deleteApplicationById(int id) {
        csApplicationService.deleteCsApplicationById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addApplicationSCMMember(int applicationId, int projectId) {
        CsApplicationScmMember pre = csApplicationScmMemberService.queryCsApplicationScmMemberByUniqueKey(applicationId, projectId);
        if (pre != null)
            return BusinessWrapper.SUCCESS;
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(projectId);
        pre = ApplicationScmMemberBuilder
                .build(applicationId, csGitlabProject);
        csApplicationScmMemberService.addCsApplicationScmMember(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public CsApplicationScmMember queryScmMemberById(int scmMemberId) {
        return csApplicationScmMemberService.queryCsApplicationScmMemberById(scmMemberId);
    }

    @Override
    public BusinessWrapper<Boolean> removeApplicationSCMMember(int id) {
        csApplicationScmMemberService.deleteCsApplicationScmMemberById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<CiJobVO.CiJob> queryCiJobPage(CiJobParam.CiJobPageQuery pageQuery) {
        DataTable<CsCiJob> table = csCiJobService.queryCsCiJobByParam(pageQuery);
        List<CiJobVO.CiJob> page = BeanCopierUtils.copyListProperties(table.getData(), CiJobVO.CiJob.class);
        return new DataTable<>(page.stream().map(e -> ciJobDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<CdJobVO.CdJob> queryCdJobPage(CdJobParam.CdJobPageQuery pageQuery) {
        DataTable<CsCdJob> table = csCdJobService.queryCsCdJobByParam(pageQuery);
        List<CdJobVO.CdJob> page = BeanCopierUtils.copyListProperties(table.getData(), CdJobVO.CdJob.class);
        return new DataTable<>(page.stream().map(e -> cdJobDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addCiJob(CiJobVO.CiJob ciJob) {
        if (!RegexUtils.isJobKeyRule(ciJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JOB_KEY_NON_COMPLIANCE_WITH_RULES);
        CsCiJob csCiJob = BeanCopierUtils.copyProperties(ciJob, CsCiJob.class);
        if (!checkJob(csCiJob.getApplicationId(), csCiJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_EXISTS);
        if (ciJob.getJobTpl() != null)
            csCiJob.setJobTplId(ciJob.getJobTpl().getId());
        csCiJobService.addCsCiJob(csCiJob);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addCdJob(CdJobVO.CdJob cdJob) {
        if (!RegexUtils.isJobKeyRule(cdJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JOB_KEY_NON_COMPLIANCE_WITH_RULES);
        CsCdJob csCdJob = BeanCopierUtils.copyProperties(cdJob, CsCdJob.class);
        if (!checkJob(csCdJob.getApplicationId(), csCdJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_EXISTS);
        if (cdJob.getJobTpl() != null)
            csCdJob.setJobTplId(cdJob.getJobTpl().getId());
        csCdJobService.addCsCdJob(csCdJob);
        // 更新部署jobId
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(cdJob.getCiJobId());
        csCiJob.setDeploymentJobId(csCdJob.getId());
        csCiJobService.updateCsCiJob(csCiJob);

        return BusinessWrapper.SUCCESS;
    }

    private boolean checkJob(int applicationId, String jobKey) {
        return csCiJobService.queryCsCiJobByUniqueKey(applicationId, jobKey) == null
                && csCdJobService.queryCsCdJobByUniqueKey(applicationId, jobKey) == null;
    }

    @Override
    public BusinessWrapper<Boolean> updateCiJob(CiJobVO.CiJob ciJob) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(ciJob.getId());
        OcUser ocUser = userFacade.getOcUserBySession();
        if (!isApplicationAdmin(csCiJob.getApplicationId(), ocUser.getId())) {
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        }
        CsCiJob pre = BeanCopierUtils.copyProperties(ciJob, CsCiJob.class);
        pre.setJobKey(csCiJob.getJobKey());
        if (ciJob.getJobTpl() != null)
            pre.setJobTplId(ciJob.getJobTpl().getId());
        csCiJobService.updateCsCiJob(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateCdJob(CdJobVO.CdJob cdJob) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(cdJob.getId());
        OcUser ocUser = userFacade.getOcUserBySession();
        if (!isApplicationAdmin(csCdJob.getApplicationId(), ocUser.getId())) {
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        }
        CsCdJob pre = BeanCopierUtils.copyProperties(cdJob, CsCdJob.class);
        pre.setJobKey(csCdJob.getJobKey());
        if (cdJob.getJobTpl() != null)
            pre.setJobTplId(cdJob.getJobTpl().getId());
        csCdJobService.updateCsCdJob(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<ApplicationVO.Engine> queryApplicationEngineByApplicationId(int applicationId) {
        List<CsApplicationEngine> list = csApplicationEngineService.queryCsApplicationEngineByApplicationId(applicationId);
        return list.stream().map(e ->
                applicationEngineDecorator.decorator(BeanCopierUtils.copyProperties(e, ApplicationVO.Engine.class), 1)
        ).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationVO.Engine> acqApplicationEngineByApplicationId(int applicationId) {
        CsApplication csApplication = csApplicationService.queryCsApplicationById(applicationId);
        if (csApplication.getEngineType() == 0) {
            List<CsApplicationEngine> list = csApplicationEngineService.selectAll();
            return list.stream().map(e ->
                    applicationEngineDecorator.decorator(BeanCopierUtils.copyProperties(e, ApplicationVO.Engine.class), 1)
            ).collect(Collectors.toList());
        } else {
            return queryApplicationEngineByApplicationId(applicationId);
        }
    }

    @Override
    public BusinessWrapper<Boolean> addApplicationEngine(int applicationId, int jenkinsInstanceId) {
        CsApplicationEngine pre = csApplicationEngineService.queryCsApplicationEngineByUniqueKey(applicationId, jenkinsInstanceId);
        if (pre != null)
            return BusinessWrapper.SUCCESS;
        pre = new CsApplicationEngine();
        pre.setApplicationId(applicationId);
        pre.setJenkinsInstanceId(jenkinsInstanceId);
        csApplicationEngineService.addCsApplicationEngine(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> removeApplicationEngine(int id) {
        csApplicationEngineService.deleteCsApplicationById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> createJobEngine(int buildType, int jobId) {
        jenkinsCiJobFacade.createJobEngine(buildType, jobId);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<JobEngineVO.JobEngine> queryJobEngine(int buildType, int jobId) {
        return jenkinsCiJobFacade.queryJobEngine(buildType, jobId);
    }

    @Override
    public BusinessWrapper<Boolean> grantUserApplication(int applicationId, int userId) {
        if (!tryApplicationAdmin(applicationId))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_NOT_ADMIN);
        if (!userPermissionFacade.tryUserBusinessPermission(userId, BusinessType.APPLICATION.getType(), applicationId)) {
            UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                    .userId(userId)
                    .businessType(BusinessType.APPLICATION.getType())
                    .businessId(applicationId)
                    .roleName("USER")
                    .build();
            userPermissionFacade.addOcUserPermission(BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class));
            grantUserJenkinsUser(userId);
        }
        return BusinessWrapper.SUCCESS;
    }

    // UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission
    private void grantUserJenkinsUser(int userId) {
        UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission = new UserBusinessGroupParam.UserUserGroupPermission();
        OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName("jenkins-users");
        if (ocUserGroup == null) return;
        userUserGroupPermission.setUserId(userId);
        userUserGroupPermission.setUserGroupId(ocUserGroup.getId());
        userFacade.grantUserUserGroup(userUserGroupPermission);
    }

    @Override
    public BusinessWrapper<Boolean> revokeUserApplication(int applicationId, int userId) {
        // 此处需要鉴权
        if (!tryApplicationAdmin(applicationId))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_NOT_ADMIN);
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userId)
                .businessType(BusinessType.APPLICATION.getType())
                .businessId(applicationId)
                .build();
        userPermissionFacade.delOcUserPermission(BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> grantUserApplicationBuildJob(int ciJobId, int userId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(ciJobId);

        if (!tryApplicationAdmin(csCiJob.getApplicationId()))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_NOT_ADMIN);
        if (!userPermissionFacade.tryUserBusinessPermission(userId, BusinessType.APPLICATION_BUILD_JOB.getType(), ciJobId)) {
            UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                    .userId(userId)
                    .businessType(BusinessType.APPLICATION_BUILD_JOB.getType())
                    .businessId(ciJobId)
                    .roleName("USER")
                    .build();
            userPermissionFacade.addOcUserPermission(BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class));
        }
        return BusinessWrapper.SUCCESS;

    }

    @Override
    public BusinessWrapper<Boolean> revokeUserApplicationBuildJob(int ciJobId, int userId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(ciJobId);
        if (!tryApplicationAdmin(csCiJob.getApplicationId()))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_NOT_ADMIN);
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userId)
                .businessType(BusinessType.APPLICATION_BUILD_JOB.getType())
                .businessId(ciJobId)
                .build();
        userPermissionFacade.delOcUserPermission(BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class));
        return BusinessWrapper.SUCCESS;
    }

    private boolean tryApplicationAdmin(int applicationId) {
        OcUser ocUser = userFacade.getOcUserBySession();
        if (userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel()).isSuccess())
            return true;
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(ocUser.getId())
                .businessType(BusinessType.APPLICATION.getType())
                .businessId(applicationId)
                .build();
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class));
        if (ocUserPermission == null)
            return false;
        return "ADMIN".equals(ocUserPermission.getRoleName());
    }

    @Override
    public BusinessWrapper<Boolean> updateUserApplicationPermission(int applicationId, int userId) {
        if (!tryApplicationAdmin(applicationId))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_NOT_ADMIN);
        UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                .userId(userId)
                .businessType(BusinessType.APPLICATION.getType())
                .businessId(applicationId)
                .build();
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(BeanCopierUtils.copyProperties(userPermissionBO, OcUserPermission.class));
        String roleName = "USER".equals(ocUserPermission.getRoleName()) ? "ADMIN" : "USER";
        ocUserPermission.setRoleName(roleName);
        ocUserPermissionService.updateOcUserPermission(ocUserPermission);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<ServerGroupVO.ServerGroup> queryServerGroupPage(ApplicationParam.ServerGroupPageQuery pageQuery) {
        if ("LOCAL".equals(pageQuery.getSource())) {
            return serverGroupFacade.queryServerGroupPage(pageQuery);
        } else {
            try {
                return opscloudServer.queryServerGroupPage(pageQuery);
            } catch (IOException e) {
                return DataTable.EMPTY;
            }
        }
    }

    @Override
    public List<ApplicationServerGroupVO.ApplicationServerGroup> queryApplicationServerGroupByApplicationId(int applicationId) {
        List<CsApplicationServerGroup> serverGroups = csApplicationServerGroupService.queryCsApplicationServerGroupByApplicationId(applicationId);
        return BeanCopierUtils.copyListProperties(serverGroups, ApplicationServerGroupVO.ApplicationServerGroup.class);
    }

    @Override
    public BusinessWrapper<Boolean> addApplicationServerGroup(ApplicationServerGroupVO.ApplicationServerGroup applicationServerGroup) {
        CsApplicationServerGroup pre = BeanCopierUtils.copyProperties(applicationServerGroup, CsApplicationServerGroup.class);
        if (csApplicationServerGroupService.queryCsApplicationServerGroupByUniqueKey(pre) != null)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SERVERGROUP_ALREADY_EXIST);
        csApplicationServerGroupService.addCsApplicationServerGroup(pre);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> removeApplicationServerGroup(int id) {
        csApplicationServerGroupService.deleteCsApplicationServerGroupById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void updateApplicationScmMember(CsGitlabProject csGitlabProject) {
        List<CsApplicationScmMember> members = csApplicationScmMemberService.queryCsApplicationScmMemberByScmId(csGitlabProject.getId());
        if (CollectionUtils.isEmpty(members)) return;
        members.forEach(m -> {
            if (!m.getScmSshUrl().equals(csGitlabProject.getSshUrl())) {
                m.setScmSshUrl(csGitlabProject.getSshUrl());
                csApplicationScmMemberService.updateCsApplicationScmMember(m);
            }
        });

    }
}
