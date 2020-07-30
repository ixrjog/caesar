package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsJobVO;
import com.baiyi.caesar.jenkins.config.JenkinsConfig;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import com.google.common.collect.Lists;
import com.offbytwo.jenkins.model.Job;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/23 2:16 下午
 * @Version 1.0
 */
@Component
public class JenkinsTplFacade {

    @Resource
    private JenkinsConfig jenkinsConfig;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsJobTplService csJobTplService;

    public String getJobContent(int instanceId, String jobName) throws IOException {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(instanceId);
        return jenkinsServerHandler.getJobXml(csJenkinsInstance.getName(), jobName);
    }

    public List<JenkinsJobVO.Job> queryJenkinsInstanceTpl(int instanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(instanceId);
        Map<String, Job> jobMap = jenkinsServerHandler.getJobs(csJenkinsInstance.getName());
        return filterTpl(instanceId, jobMap);
    }

    private List<JenkinsJobVO.Job> filterTpl(int instanceId, Map<String, Job> jobMap) {
        List<JenkinsJobVO.Job> jobs = Lists.newArrayList();
        if (jobMap.isEmpty())
            return jobs;
        Set<String> jobNameSet = acqCsJobTplByInstanceId(instanceId);
        jobMap.keySet().forEach(k -> {
            if (k.indexOf(jenkinsConfig.getTemplate().getPrefix()) == 0 && !jobNameSet.contains(k))
                jobs.add(BeanCopierUtils.copyProperties(jobMap.get(k), JenkinsJobVO.Job.class));
        });
        return jobs;
    }

    private Set<String> acqCsJobTplByInstanceId(int instanceId) {
        return csJobTplService.queryCsJobTplByInstanceId(instanceId).stream().map(CsJobTpl::getTplName).collect(Collectors.toSet());
    }

}

