package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsJobVO;
import com.baiyi.caesar.jenkins.config.JenkinsConfig;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.google.common.collect.Lists;
import com.offbytwo.jenkins.model.Job;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    public String getJobContent(int instanceId, String jobName) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(instanceId);
        return jenkinsServerHandler.getJobXml(csJenkinsInstance.getName(), jobName);
    }

    public List<JenkinsJobVO.Job> queryJenkinsInstanceTpl(int instanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(instanceId);
        Map<String, Job> jobMap = jenkinsServerHandler.getJobs(csJenkinsInstance.getName());
        return filterTpl(jobMap);
    }

    private List<JenkinsJobVO.Job> filterTpl(Map<String, Job> jobMap) {
        List<JenkinsJobVO.Job> jobs = Lists.newArrayList();
        if (jobMap.isEmpty())
            return jobs;
        jobMap.keySet().forEach(k -> {
            if (k.indexOf(jenkinsConfig.getTemplate().getPrefix()) == 0)
                jobs.add(BeanCopierUtils.copyProperties(jobMap.get(k), JenkinsJobVO.Job.class));
        });
        return jobs;
    }

}

