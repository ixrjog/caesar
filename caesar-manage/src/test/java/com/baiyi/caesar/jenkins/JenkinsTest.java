package com.baiyi.caesar.jenkins;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.facade.jenkins.JenkinsJobFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsTplFacade;
import com.baiyi.caesar.jenkins.config.JenkinsConfig;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
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

    @Resource
    private JenkinsJobFacade jenkinsCiJobFacade;

    @Test
    void versionTest() {
        JenkinsVersion jenkinsVersion = jenkinsServerHandler.getVersion("master-2");

        System.err.println( StringUtils.isBlank(jenkinsVersion.getLiteralVersion()));

        System.err.println(JSON.toJSON(jenkinsVersion));
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
    void jobTest() {
        JobWithDetails job = jenkinsServerHandler.getJob("master-1", "CAESAR_caesar-server-prod");
        System.err.println(job);
        job.getBuildByNumber(9);

        Build build = job.getLastBuild();
        try {
            BuildWithDetails buildWithDetails = build.details();
            String consoleOutputHtml = buildWithDetails.getConsoleOutputHtml();

            //  System.err.println(consoleOutputHtml);
            System.err.println(buildWithDetails.getConsoleOutputText());
            List<BuildCause> list = buildWithDetails.getCauses();
            String builtOn = buildWithDetails.getBuiltOn();
            System.err.println(builtOn);
            System.err.println(list);
            System.err.println(buildWithDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void computerTest() {
        try {
            Map<String, Computer> map = jenkinsServerHandler.getComputerMap("master-1");
            System.err.println(map);
            Computer c = map.get("node-06.ops.yangege.cn");
            ComputerWithDetails computerWithDetails = c.details();
            System.err.println(computerWithDetails);
            computerWithDetails.getExecutors().forEach(e -> {
                e.getCurrentExecutable();
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateJobEngine() {
        // jenkinsCiJobFacade.createJobEngine(2);
    }

    @Test
    void queryJenkinsInstanceTplTest() {
    }

}