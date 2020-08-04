package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.builder.ApplicationScmMemberBuilder;
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
import com.baiyi.caesar.facade.jenkins.JenkinsCiJobFacade;
import com.baiyi.caesar.service.application.CsApplicationEngineService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
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

    @Override
    public DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery) {
        DataTable<CsApplication> table = csApplicationService.queryCsApplicationByParam(pageQuery);
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

}
