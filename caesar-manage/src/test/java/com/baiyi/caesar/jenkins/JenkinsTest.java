package com.baiyi.caesar.jenkins;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.facade.jenkins.JenkinsTplFacade;
import com.baiyi.caesar.jenkins.config.JenkinsConfig;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Job;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/18 2:08 下午
 * @Version 1.0
 */
public class JenkinsTest extends BaseUnit {

    @Resource
    private JenkinsConfig jenkinsConfig;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private JenkinsTplFacade jenkinsTplFacade;

    @Test
    void versionTest() {
        JenkinsVersion jenkinsVersion = jenkinsServerHandler.getVersion("master-1");
        System.err.println(jenkinsVersion);
    }

    @Test
    void jenkinsConfigTest() {
        System.err.println(jenkinsConfig);
    }

    @Test
    void jenkinsTest() {
        Map<String, Job> jobMap = jenkinsServerHandler.getJobs("master-2");
        System.err.println(jobMap);
    }

    @Test
    void queryJenkinsInstanceTplTest() {
    }

}