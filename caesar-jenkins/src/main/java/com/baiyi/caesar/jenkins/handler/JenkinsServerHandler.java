package com.baiyi.caesar.jenkins.handler;

import com.baiyi.caesar.jenkins.server.JenkinsServerContainer;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/17 4:16 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsServerHandler {

    public JenkinsVersion getVersion(String serverName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
            assert jenkinsServer != null;
            return jenkinsServer.getVersion();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取job
     *
     * @param jobName
     * @return
     */
    public Job getJob(String serverName, String jobName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
            assert jenkinsServer != null;
            return jenkinsServer.getJob(jobName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJobXml(String serverName, String jobName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
            assert jenkinsServer != null;
            return jenkinsServer.getJobXml(jobName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Job> getJobs(String serverName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
            assert jenkinsServer != null;
            return jenkinsServer.getJobs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }


}
