package com.baiyi.caesar.jenkins.handler;

import com.baiyi.caesar.jenkins.server.JenkinsServerContainer;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
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

//    @Resource
//    private JenkinsServerContainer jenkinsServerContainer;

    public static final boolean CRUMB_FLAG = true;

    public JenkinsVersion getVersion(String serverName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
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
    public JobWithDetails getJob(String serverName, String jobName) {
        try {
            return JenkinsServerContainer.getJenkinsServer(serverName).getJob(jobName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Computer> getComputerMap(String serverName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
            return jenkinsServer.getComputers();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateJob(String serverName, String jobName, String jobXml) throws IOException {
        JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
        assert jenkinsServer != null;
        jenkinsServer.updateJob(jobName, jobXml, CRUMB_FLAG);
    }


    public String getJobXml(String serverName, String jobName) throws IOException {
        JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
        assert jenkinsServer != null;
        return jenkinsServer.getJobXml(jobName);
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


    public void createJob(String serverName, String jobName, String jobXml) throws IOException {
        JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
        assert jenkinsServer != null;
        jenkinsServer.createJob(jobName, jobXml, CRUMB_FLAG);
    }

}
