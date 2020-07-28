package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.HashUtils;
import com.baiyi.caesar.decorator.jenkins.JenkinsInstanceDecorator;
import com.baiyi.caesar.decorator.jenkins.JobTplDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.param.jenkins.JenkinsInstanceParam;
import com.baiyi.caesar.domain.param.jenkins.JobTplParam;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsJobVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.facade.JenkinsFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsTplFacade;
import com.baiyi.caesar.jenkins.server.JenkinsServerContainer;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public DataTable<JenkinsInstanceVO.Instance> queryJenkinsInstancePage(JenkinsInstanceParam.JenkinsInstancePageQuery pageQuery) {
        DataTable<CsJenkinsInstance> table = csJenkinsInstanceService.queryCsJenkinsInstanceByParam(pageQuery);
        List<JenkinsInstanceVO.Instance> page = BeanCopierUtils.copyListProperties(table.getData(), JenkinsInstanceVO.Instance.class);
        return new DataTable<>(page.stream().map(e -> jenkinsInstanceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
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
        try{
            String xml = jenkinsTplFacade.getJobContent(jobTpl.getJenkinsInstanceId(), jobTpl.getTplName());
            csJobTpl.setTplContent(xml);
            csJobTpl.setTplHash(HashUtils.MD5(xml));
        }catch (Exception ignored){
        }
        csJobTplService.addCsJobTpl(csJobTpl);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateJobTpl(JobTplVO.JobTpl jobTpl) {
        CsJobTpl csJobTpl = BeanCopierUtils.copyProperties(jobTpl, CsJobTpl.class);
        csJobTplService.updateCsJobTpl(csJobTpl);
        return BusinessWrapper.SUCCESS;
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
}
