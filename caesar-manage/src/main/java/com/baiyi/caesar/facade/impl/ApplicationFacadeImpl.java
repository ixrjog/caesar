package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.bo.UserPermissionBO;
import com.baiyi.caesar.builder.ApplicationScmMemberBuilder;
import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.RegexUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.packer.application.*;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CdJobParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.param.user.UserBusinessGroupParam;
import com.baiyi.caesar.domain.vo.application.*;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.facade.*;
import com.baiyi.caesar.facade.jenkins.JobEngineFacade;
import com.baiyi.caesar.facade.jenkins.factory.IJobEngine;
import com.baiyi.caesar.facade.jenkins.factory.JobEngineFactory;
import com.baiyi.caesar.opscloud.OpscloudServer;
import com.baiyi.caesar.service.application.*;
import com.baiyi.caesar.service.gitlab.CsGitlabGroupService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.user.OcUserGroupService;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import com.baiyi.caesar.service.user.OcUserService;
import org.springframework.ldap.AttributeInUseException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.EXTEND;

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
    private CsApplicationScmGroupService csApplicationScmGroupService;

    @Resource
    private CsApplicationEngineService csApplicationEngineService;

    @Resource
    private ApplicationPacker applicationDecorator;

    @Resource
    private ApplicationScmMemberPacker applicationScmMemberWrap;

    @Resource
    private ApplicationScmGroupPacker applicationScmGroupWrap;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsCdJobService csCdJobService;

    @Resource
    private CiJobPacker ciJobDecorator;

    @Resource
    private CdJobPacker cdJobDecorator;

    @Resource
    private ApplicationEnginePacker applicationEngineDecorator;

    @Resource
    private JobEngineFacade jenkinsCiJobFacade;

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

    @Resource
    private EnvFacade envFacade;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private CsGitlabGroupService csGitlabGroupService;


    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<CsApplication> table = csApplicationService.queryCsApplicationByParam(pageQuery);
        List<ApplicationVO.Application> page = BeanCopierUtil.copyListProperties(table.getData(), ApplicationVO.Application.class);
        return new DataTable<>(page.stream().map(e -> applicationDecorator.wrap(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    /**
     * Caesar管理员可查看所有应用
     *
     * @param pageQuery
     * @return
     */
    @Override
    public DataTable<ApplicationVO.Application> queryMyApplicationPage(ApplicationParam.MyApplicationPageQuery pageQuery) {
        OcUser ocUser = userFacade.getOcUserBySession();
        pageQuery.setUserId(ocUser.getId());
        boolean queryAll = false;
        if (pageQuery.getIsAll() != null && pageQuery.getIsAll()) {
            if (userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel()).isSuccess())
                queryAll = true;
        }
        DataTable<CsApplication> table;
        if (queryAll) {
            table = csApplicationService.queryCsApplicationByParam(BeanCopierUtil.copyProperties(pageQuery, ApplicationParam.ApplicationPageQuery.class));
        } else {
            table = csApplicationService.queryMyCsApplicationByParam(pageQuery);
        }
        List<ApplicationVO.Application> page = BeanCopierUtil.copyListProperties(table.getData(), ApplicationVO.Application.class);
        return new DataTable<>(page.stream().map(e -> applicationDecorator.wrap(e, ocUser, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public List<ApplicationVO.ScmMember> queryApplicationScmMemberByApplicationId(int applicationId) {
        return csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(applicationId)
                .stream().map(e -> applicationScmMemberWrap.wrap(BeanCopierUtil.copyProperties(e, ApplicationVO.ScmMember.class))).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationVO.ScmGroup> queryApplicationScmGroupByApplicationId(int applicationId) {
        return csApplicationScmGroupService.queryApplicationScmGroupByApplicationId(applicationId)
                .stream().map(e -> applicationScmGroupWrap.wrap(BeanCopierUtil.copyProperties(e, ApplicationVO.ScmGroup.class))).collect(Collectors.toList());
    }

    @Override
    public BusinessWrapper<Boolean> addApplication(ApplicationVO.Application application) {
        if (!RegexUtil.isApplicationKeyRule(application.getApplicationKey()))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_KEY_NON_COMPLIANCE_WITH_RULES);
        CsApplication csApplication = BeanCopierUtil.copyProperties(application, CsApplication.class);
        csApplicationService.addCsApplication(csApplication);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateApplication(ApplicationVO.Application application) {
        CsApplication pre = BeanCopierUtil.copyProperties(application, CsApplication.class);
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
        // scmMember
        if (!CollectionUtils.isEmpty(csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(id)))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SCM_CONFIGURATION_WAS_NOT_DELETED);
        // serverGroup
        if (!CollectionUtils.isEmpty(csApplicationServerGroupService.queryCsApplicationServerGroupByApplicationId(id)))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SERVERGROUP_CONFIGURATION_WAS_NOT_DELETED);
        // applicationEngine
        if (!CollectionUtils.isEmpty(csApplicationEngineService.queryCsApplicationEngineByApplicationId(id)))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_ENGINE_CONFIGURATION_WAS_NOT_DELETED);
        // job
        if (csCiJobService.countCsCiJobByApplicationId(id) != 0)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_BUILD_JOB_CONFIGURATION_WAS_NOT_DELETED);
        if (csCdJobService.countCsCdJobByApplicationId(id) != 0)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_DEPLOYMENT_JOB_CONFIGURATION_WAS_NOT_DELETED);
        // 删除应用授权
        userPermissionFacade.cleanBusinessPermission(BusinessType.APPLICATION.getType(), id);
        csApplicationService.deleteCsApplicationById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void addApplicationSCMMember(int applicationId, int projectId) {
        if (csApplicationScmMemberService.queryCsApplicationScmMemberByUniqueKey(applicationId, projectId) != null)
            return;
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(projectId);
        CsApplicationScmMember pre = ApplicationScmMemberBuilder
                .build(applicationId, csGitlabProject);
        csApplicationScmMemberService.addCsApplicationScmMember(pre);
    }

    @Override
    public void addApplicationSCMGroup(int applicationId, int groupId) {
        if (csApplicationScmGroupService.queryCsApplicationScmGroupByUniqueKey(applicationId, groupId) != null)
            return;
        CsApplicationScmGroup pre = new CsApplicationScmGroup();
        pre.setApplicationId(applicationId);
        pre.setGroupId(groupId);
        csApplicationScmGroupService.add(pre);
    }

    @Override
    public void removeApplicationSCMGroup(int id) {
        csApplicationScmGroupService.deleteById(id);
    }

    @Override
    public CsApplicationScmMember queryScmMemberById(int scmMemberId) {
        return csApplicationScmMemberService.queryCsApplicationScmMemberById(scmMemberId);
    }

    @Override
    public void removeApplicationSCMMember(int id) {
        csApplicationScmMemberService.deleteCsApplicationScmMemberById(id);
    }

    @Override
    public DataTable<CiJobVO.CiJob> queryCiJobPage(CiJobParam.CiJobPageQuery pageQuery) {
        if (pageQuery.getShowHide() == null)
            pageQuery.setShowHide(false);
        DataTable<CsCiJob> table = csCiJobService.queryCsCiJobByParam(pageQuery);
        List<CiJobVO.CiJob> page = BeanCopierUtil.copyListProperties(table.getData(), CiJobVO.CiJob.class);
        return new DataTable<>(page.stream().map(e -> ciJobDecorator.wrap(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<CdJobVO.CdJob> queryCdJobPage(CdJobParam.CdJobPageQuery pageQuery) {
        DataTable<CsCdJob> table = csCdJobService.queryCsCdJobByParam(pageQuery);
        List<CdJobVO.CdJob> page = BeanCopierUtil.copyListProperties(table.getData(), CdJobVO.CdJob.class);
        return new DataTable<>(page.stream().map(e -> cdJobDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addCiJob(CiJobVO.CiJob ciJob) {
        if (!RegexUtil.isJobKeyRule(ciJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JOB_KEY_NON_COMPLIANCE_WITH_RULES);
        CsCiJob csCiJob = BeanCopierUtil.copyProperties(ciJob, CsCiJob.class);
        if (!checkJob(csCiJob.getApplicationId(), csCiJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_EXISTS);
        if (ciJob.getJobTpl() != null)
            csCiJob.setJobTplId(ciJob.getJobTpl().getId());
        csCiJobService.addCsCiJob(csCiJob);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addCdJob(CdJobVO.CdJob cdJob) {
        if (!RegexUtil.isJobKeyRule(cdJob.getJobKey()))
            return new BusinessWrapper<>(ErrorEnum.JOB_KEY_NON_COMPLIANCE_WITH_RULES);
        CsCdJob csCdJob = BeanCopierUtil.copyProperties(cdJob, CsCdJob.class);
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
        boolean isUpdateJobEngine = ciJob.getJobTpl() != null && !ciJob.getJobTpl().getId().equals(csCiJob.getJobTplId());
        OcUser ocUser = userFacade.getOcUserBySession();
        if (!isApplicationAdmin(csCiJob.getApplicationId(), ocUser.getId()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        CsCiJob pre = BeanCopierUtil.copyProperties(ciJob, CsCiJob.class);
        pre.setJobKey(csCiJob.getJobKey());
        if (ciJob.getJobTpl() != null)
            pre.setJobTplId(ciJob.getJobTpl().getId());
        csCiJobService.updateCsCiJob(pre);
        if (isUpdateJobEngine) {
            IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(BuildType.BUILD.getType());
            iJobEngine.updateJobEngine(csCiJob.getId());
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateCdJob(CdJobVO.CdJob cdJob) {
        CsCdJob csCdJob = csCdJobService.queryCsCdJobById(cdJob.getId());
        boolean isUpdateJobEngine = cdJob.getJobTplId() != null && !cdJob.getJobTpl().getId().equals(csCdJob.getJobTplId());
        OcUser ocUser = userFacade.getOcUserBySession();
        if (!isApplicationAdmin(csCdJob.getApplicationId(), ocUser.getId()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        CsCdJob pre = BeanCopierUtil.copyProperties(cdJob, CsCdJob.class);
        pre.setJobKey(csCdJob.getJobKey());
        if (cdJob.getJobTpl() != null)
            pre.setJobTplId(cdJob.getJobTpl().getId());
        csCdJobService.updateCsCdJob(pre);
        if (isUpdateJobEngine) {
            IJobEngine iJobEngine = JobEngineFactory.getJobEngineByKey(BuildType.DEPLOYMENT.getType());
            iJobEngine.updateJobEngine(csCdJob.getId());
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<ApplicationVO.Engine> queryApplicationEngineByApplicationId(int applicationId) {
        List<CsApplicationEngine> list = csApplicationEngineService.queryCsApplicationEngineByApplicationId(applicationId);
        return list.stream().map(e ->
                applicationEngineDecorator.decorator(BeanCopierUtil.copyProperties(e, ApplicationVO.Engine.class), EXTEND)
        ).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationVO.Engine> acqApplicationEngineByApplicationId(int applicationId) {
        CsApplication csApplication = csApplicationService.queryCsApplicationById(applicationId);
        if (csApplication.getEngineType() == 0) {
            List<CsApplicationEngine> list = csApplicationEngineService.selectAll();
            return list.stream().map(e ->
                    applicationEngineDecorator.decorator(BeanCopierUtil.copyProperties(e, ApplicationVO.Engine.class), EXTEND)
            ).collect(Collectors.toList());
        } else {
            return queryApplicationEngineByApplicationId(applicationId);
        }
    }

    @Override
    public BusinessWrapper<Boolean> addApplicationEngine(int applicationId, int jenkinsInstanceId) {
        if (csApplicationEngineService.queryCsApplicationEngineByUniqueKey(applicationId, jenkinsInstanceId) != null)
            return BusinessWrapper.SUCCESS;
        CsApplicationEngine pre = new CsApplicationEngine();
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
        return jenkinsCiJobFacade.queryJobEngines(buildType, jobId);
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
            userPermissionFacade.addOcUserPermission(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
            try {
                grantUserJenkinsUser(userId);
            } catch (AttributeInUseException ignored) {
            }
        }
        return BusinessWrapper.SUCCESS;
    }

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
        userPermissionFacade.delOcUserPermission(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> grantUserApplicationBuildJob(int ciJobId, int userId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(ciJobId);
        if (!tryApplicationAdmin(csCiJob.getApplicationId()))
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_NOT_ADMIN);
        // 测试总监权限控制
        BusinessWrapper<Boolean> wrapper = enhanceAuthentication(csCiJob);
        if (!wrapper.isSuccess())
            return wrapper;

        if (!userPermissionFacade.tryUserBusinessPermission(userId, BusinessType.APPLICATION_BUILD_JOB.getType(), ciJobId)) {
            UserPermissionBO userPermissionBO = UserPermissionBO.builder()
                    .userId(userId)
                    .businessType(BusinessType.APPLICATION_BUILD_JOB.getType())
                    .businessId(ciJobId)
                    .roleName("USER")
                    .build();
            userPermissionFacade.addOcUserPermission(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
        }
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 测试总监权限控制
     *
     * @param csCiJob
     * @return
     */
    @Deprecated
    private BusinessWrapper<Boolean> enhanceAuthentication(CsCiJob csCiJob) {
        if (!csCiJob.getJobType().equals(JobTypeEnum.JAVA.getType()))
            return BusinessWrapper.SUCCESS;
        if (!"daily".equals(envFacade.queryEnvNameByType(csCiJob.getEnvType())))
            return BusinessWrapper.SUCCESS;

        // 应用管理员 tonghongping 童红平
        String username = SessionUtil.getUsername();

        // 是否测试总监配置
        if ("tonghongping".equals(username)) {
            return BusinessWrapper.SUCCESS;
        } else {
            OcUser ocUser = ocUserService.queryOcUserByUsername("tonghongping");
            // 非总监
            OcUserPermission ocUserPermission
                    = userPermissionFacade.queryUserPermissionByUniqueKey(ocUser.getId(), BusinessType.APPLICATION.getType(), csCiJob.getApplicationId());
            if (ocUserPermission == null || "USER".equalsIgnoreCase(ocUserPermission.getRoleName()))
                return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.APPLICATION_JOB_AUTHENTICATION_FAILUER);
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
        userPermissionFacade.delOcUserPermission(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
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
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
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
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(BeanCopierUtil.copyProperties(userPermissionBO, OcUserPermission.class));
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
        return BeanCopierUtil.copyListProperties(serverGroups, ApplicationServerGroupVO.ApplicationServerGroup.class);
    }

    @Override
    public BusinessWrapper<Boolean> addApplicationServerGroup(ApplicationServerGroupVO.ApplicationServerGroup applicationServerGroup) {
        CsApplicationServerGroup pre = BeanCopierUtil.copyProperties(applicationServerGroup, CsApplicationServerGroup.class);
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
        members.parallelStream().forEach(m -> {
            if (!m.getScmSshUrl().equals(csGitlabProject.getSshUrl())) {
                m.setScmSshUrl(csGitlabProject.getSshUrl());
                csApplicationScmMemberService.updateCsApplicationScmMember(m);
            }
        });

    }

    public void syncApplicationScmMember(int applicationId) {
        List<CsApplicationScmGroup> scmGroups = csApplicationScmGroupService.queryApplicationScmGroupByApplicationId(applicationId);
        if (CollectionUtils.isEmpty(scmGroups)) return;
        scmGroups.forEach(e -> {
            CsGitlabGroup gitlabGroup = csGitlabGroupService.queryCsGitlabGroupById(e.getGroupId());
            if (gitlabGroup == null) return;
            csGitlabProjectService.queryCsGitlabProjectByInstanceIdAndNamespacePath(gitlabGroup.getInstanceId(), gitlabGroup.getPath()).forEach(p -> {
                if (csApplicationScmMemberService.queryCsApplicationScmMemberByUniqueKey(e.getApplicationId(), p.getId()) == null) {
                    CsApplicationScmMember scmMember = new CsApplicationScmMember();
                    scmMember.setApplicationId(e.getApplicationId());
                    scmMember.setScmId(p.getId());
                    scmMember.setScmType("GITLAB");
                    scmMember.setScmSshUrl(p.getSshUrl());
                    scmMember.setComment(p.getDescription());
                    csApplicationScmMemberService.addCsApplicationScmMember(scmMember);
                }
            });

        });
    }
}
