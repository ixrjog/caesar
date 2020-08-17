package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.bo.UserPermissionBO;
import com.baiyi.caesar.builder.ApplicationScmMemberBuilder;
import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.decorator.application.ApplicationDecorator;
import com.baiyi.caesar.decorator.application.ApplicationEngineDecorator;
import com.baiyi.caesar.decorator.application.ApplicationScmMemberDecorator;
import com.baiyi.caesar.decorator.application.CiJobDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.GitlabFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsCiJobFacade;
import com.baiyi.caesar.service.application.CsApplicationEngineService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.user.OcUserPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private CiJobDecorator ciJobDecorator;

    @Resource
    private GitlabFacade gitlabFacade;

    @Resource
    private ApplicationEngineDecorator applicationEngineDecorator;

    @Resource
    private JenkinsCiJobFacade jenkinsCiJobFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    @Resource
    private UserFacade userFacade;

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
        return new DataTable<>(page.stream().map(e -> applicationDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public List<ApplicationVO.ScmMember> queryApplicationScmMemberByApplicationId(int applicationId) {
        return csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(applicationId)
                .stream().map(e -> applicationScmMemberDecorator.decorator(BeanCopierUtils.copyProperties(e, ApplicationVO.ScmMember.class), 1)).collect(Collectors.toList());
    }

    @Override
    public BusinessWrapper<Boolean> addApplication(ApplicationVO.Application application) {
        CsApplication csApplication = BeanCopierUtils.copyProperties(application, CsApplication.class);
        csApplicationService.addCsApplication(csApplication);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateApplication(ApplicationVO.Application application) {
        CsApplication csApplication = BeanCopierUtils.copyProperties(application, CsApplication.class);
        csApplicationService.updateCsApplication(csApplication);
        return BusinessWrapper.SUCCESS;
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
    public BusinessWrapper<Boolean> addCiJob(CiJobVO.CiJob ciJob) {
        CsCiJob csCiJob = BeanCopierUtils.copyProperties(ciJob, CsCiJob.class);
        if (ciJob.getJobTpl() != null)
            csCiJob.setJobTplId(ciJob.getJobTpl().getId());
        csCiJobService.addCsCiJob(csCiJob);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateCiJob(CiJobVO.CiJob ciJob) {
        CsCiJob csCiJob = BeanCopierUtils.copyProperties(ciJob, CsCiJob.class);
        if (IDUtils.isEmpty(csCiJob.getJobTplId()) && ciJob.getJobTpl() != null)
            csCiJob.setJobTplId(ciJob.getJobTpl().getId());
        csCiJobService.updateCsCiJob(csCiJob);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(ApplicationParam.ScmMemberBranchQuery scmMemberBranchQuery) {
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(scmMemberBranchQuery.getScmMemberId());
        if (csApplicationScmMember == null)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SCM_NOT_EXIST);
        return gitlabFacade.queryGitlabProjectRepository(csApplicationScmMember.getScmId(), scmMemberBranchQuery.getEnableTag());
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
    public BusinessWrapper<Boolean> createCiJobEngine(int ciJobId) {
        jenkinsCiJobFacade.createJobEngine(ciJobId);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<CiJobVO.JobEngine> queryCiJobEngine(int ciJobId) {
        return jenkinsCiJobFacade.queryJobEngine(ciJobId);
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
        }
        return BusinessWrapper.SUCCESS;
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

}
