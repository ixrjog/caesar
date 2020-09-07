package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.HashUtils;
import com.baiyi.caesar.decorator.application.CiJobDecorator;
import com.baiyi.caesar.decorator.jenkins.JenkinsInstanceDecorator;
import com.baiyi.caesar.decorator.jenkins.JobTplDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.param.jenkins.JenkinsInstanceParam;
import com.baiyi.caesar.domain.param.jenkins.JobTplParam;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsJobVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.facade.JenkinsFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsTplFacade;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.jenkins.server.JenkinsServerContainer;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobEngineService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/17 5:48 下午
 * @Version 1.0
 */
@Service
public class JenkinsFacadeImpl implements JenkinsFacade {

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private CsJobTplService csJobTplService;

    @Resource
    private JenkinsInstanceDecorator jenkinsInstanceDecorator;

    @Resource
    private JenkinsServerContainer jenkinsServerContainer;

    @Resource
    private JobTplDecorator jobTplDecorator;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private JenkinsTplFacade jenkinsTplFacade;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CiJobDecorator ciJobDecorator;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Override
    public DataTable<JenkinsInstanceVO.Instance> queryJenkinsInstancePage(JenkinsInstanceParam.JenkinsInstancePageQuery pageQuery) {
        DataTable<CsJenkinsInstance> table = csJenkinsInstanceService.queryCsJenkinsInstanceByParam(pageQuery);
        List<JenkinsInstanceVO.Instance> page = BeanCopierUtils.copyListProperties(table.getData(), JenkinsInstanceVO.Instance.class);
        return new DataTable<>(page.stream().map(e -> jenkinsInstanceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> setJenkinsInstanceActive(int id) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(id);
        csJenkinsInstance.setIsActive(!csJenkinsInstance.getIsActive());
        csJenkinsInstanceService.updateCsJenkinsInstance(csJenkinsInstance);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> addJenkinsInstance(JenkinsInstanceVO.Instance instance) {
        CsJenkinsInstance csJenkinsInstance = BeanCopierUtils.copyProperties(instance, CsJenkinsInstance.class);
        csJenkinsInstance.setToken(stringEncryptor.encrypt(instance.getToken()));
        csJenkinsInstanceService.addCsJenkinsInstance(csJenkinsInstance);
        jenkinsServerContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateJenkinsInstance(JenkinsInstanceVO.Instance instance) {
        CsJenkinsInstance pre = BeanCopierUtils.copyProperties(instance, CsJenkinsInstance.class);
        if (StringUtils.isEmpty(pre.getToken())) {
            CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(instance.getId());
            pre.setToken(csJenkinsInstance.getToken());
        } else {
            pre.setToken(stringEncryptor.encrypt(instance.getToken()));
        }
        csJenkinsInstanceService.updateCsJenkinsInstance(pre);
        jenkinsServerContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteJenkinsInstanceById(int id) {
        csJenkinsInstanceService.deleteCsJenkinsInstanceById(id);
        jenkinsServerContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<JobTplVO.JobTpl> queryJobTplPage(JobTplParam.JobTplPageQuery pageQuery) {
        DataTable<CsJobTpl> table = csJobTplService.queryCsJobTplByParam(pageQuery);
        List<JobTplVO.JobTpl> page = BeanCopierUtils.copyListProperties(table.getData(), JobTplVO.JobTpl.class);
        return new DataTable<>(page.stream().map(e -> jobTplDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addJobTpl(JobTplVO.JobTpl jobTpl) {
        CsJobTpl csJobTpl = BeanCopierUtils.copyProperties(jobTpl, CsJobTpl.class);
        try {
            String xml = jenkinsTplFacade.getJobContent(jobTpl.getJenkinsInstanceId(), jobTpl.getTplName());
            csJobTpl.setTplContent(xml);
            csJobTpl.setTplHash(HashUtils.MD5(xml));
        } catch (Exception ignored) {
        }
        csJobTplService.addCsJobTpl(csJobTpl);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateJobTpl(JobTplVO.JobTpl jobTpl) {
        saveJobTpl(jobTpl);
        return BusinessWrapper.SUCCESS;
    }

    private void saveJobTpl(JobTplVO.JobTpl jobTpl) {
        CsJobTpl csJobTpl = BeanCopierUtils.copyProperties(jobTpl, CsJobTpl.class);
        csJobTpl.setTplVersion(csJobTpl.getTplVersion() + 1); // 递增版本号
        csJobTplService.updateCsJobTpl(csJobTpl);
    }

    @Override
    public BusinessWrapper<Boolean> writeJobTpl(JobTplVO.JobTpl jobTpl) {
        try {
            jenkinsTplFacade.updateJobContent(jobTpl.getJenkinsInstanceId(), jobTpl.getTplName(), jobTpl.getTplContent());
            saveJobTpl(jobTpl);
            return BusinessWrapper.SUCCESS;
        } catch (IOException e) {
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_TPL_WRITE_ERROR);
        }
    }

    @Override
    public BusinessWrapper<Boolean> deleteJobTplById(int id) {
        csJobTplService.deleteCsJobTplById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<JenkinsJobVO.Job> queryJobTplByInstanceId(int instanceId) {
        return jenkinsTplFacade.queryJenkinsInstanceTpl(instanceId);
    }

    @Override
    public BusinessWrapper<Boolean> readJobTplById(int id) {
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(id);
        try {
            String xml = jenkinsTplFacade.getJobContent(csJobTpl.getJenkinsInstanceId(), csJobTpl.getTplName());
            return new BusinessWrapper(xml);
        } catch (IOException e) {
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_TPL_READ_ERROR);
        }
    }

    @Override
    public DataTable<CiJobVO.CiJob> queryCiJobTplPage(CiJobParam.CiJobTplPageQuery pageQuery) {
        DataTable<CsCiJob> table = csCiJobService.queryCsCiJobByParam(pageQuery);
        List<CiJobVO.CiJob> page = BeanCopierUtils.copyListProperties(table.getData(), CiJobVO.CiJob.class);
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(pageQuery.getJobTplId());
        return new DataTable<>(page.stream().map(e -> ciJobDecorator.decorator(e, csJobTpl)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> upgradeCiJobTplByJobId(int jobId) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(jobId);
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(csCiJob.getJobTplId());
        List<CsJobEngine> csJobEngines = csJobEngineService.queryCsJobEngineByJobId(BuildType.BUILD.getType(), jobId);
        if (!CollectionUtils.isEmpty(csJobEngines))
            csJobEngines.forEach(e -> {
                if (csJobTpl.getTplVersion() > e.getTplVersion()) {
                    try {
                        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(e.getJenkinsInstanceId());
                        if (jenkinsServerHandler.tryJob(csJenkinsInstance.getName(), e.getName())) {
                            jenkinsServerHandler.updateJob(csJenkinsInstance.getName(), e.getName(), csJobTpl.getTplContent());
                        } else {
                            jenkinsServerHandler.createJob(csJenkinsInstance.getName(), e.getName(), csJobTpl.getTplContent());
                        }
                        e.setTplVersion(csJobTpl.getTplVersion());
                        e.setTplHash(csJobTpl.getTplHash());
                        csJobEngineService.updateCsJobEngine(e);
                    } catch (IOException ignored) {
                    }
                }
            });
        return BusinessWrapper.SUCCESS;
    }


}
