package com.baiyi.caesar.jenkins.handler;

import com.baiyi.caesar.common.base.BuildOutputType;
import com.baiyi.caesar.jenkins.server.JenkinsServerContainer;
import com.google.common.collect.Maps;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

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

    public String getBuildOutputByType(JobWithDetails job, int buildNumber, int outputType) throws IOException {
        Build build = job.getBuildByNumber(buildNumber);
        BuildWithDetails buildWithDetails = build.details();
        if (outputType == BuildOutputType.HTML.getType()) {
            return buildWithDetails.getConsoleOutputHtml();
        } else {
            return buildWithDetails.getConsoleOutputText();
        }
    }

    public boolean tryJob(String serverName, String jobName) {
        try {
            return Objects.requireNonNull(JenkinsServerContainer.getJenkinsServer(serverName)).getJob(jobName) != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Map<String, Computer> getComputerMap(String serverName) {
        try {
            JenkinsServer jenkinsServer = JenkinsServerContainer.getJenkinsServer(serverName);
            assert jenkinsServer != null;
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
