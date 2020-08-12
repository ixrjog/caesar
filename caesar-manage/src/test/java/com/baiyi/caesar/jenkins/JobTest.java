package com.baiyi.caesar.jenkins;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.TimeUtils;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.jenkins.server.JenkinsServerContainer;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @Author baiyi
 * @Date 2020/8/6 4:01 下午
 * @Version 1.0
 */
public class JobTest extends BaseUnit {

    @Resource
    private JobFacade jobFacade;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Test
    void testBuild() {
        JobBuildParam.CiBuildParam buildParam = new JobBuildParam.CiBuildParam();
        buildParam.setCiJobId(2);
        jobFacade.buildCiJob(buildParam);
    }

    @Test
    void testAcqJenkinsDate() {
        String date = "2020-08-10 16:32:26 +0800";
        System.err.println(TimeUtils.acqJenkinsDate(date));
    }

    @Test
    void testRandom() {
        int x = 0;
        while (true) {
            Random random = new Random();
            int n = random.nextInt(2);
            System.err.println(n);
            x++;
            if (x > 100) break;
        }
    }

    @Resource
    private JenkinsServerContainer jenkinsServerContainer;

    @Test
    void testJenkins() {
        int x = 0;
        while (true) {

            try {
                JenkinsServer jenkinsServer1 = jenkinsServerContainer.getJenkinsServer("master-1");
                JobWithDetails job1 = jenkinsServer1.getJob("CAESAR_caesar-web-prod");
                System.err.println(job1);
                jenkinsServer1.close();

                JenkinsServer jenkinsServer2 = jenkinsServerContainer.getJenkinsServer("master-2");
                JobWithDetails job2 = jenkinsServer2.getJob("CAESAR_caesar-web-prod");
                System.err.println(job2);
                jenkinsServer2.close();


                try{
                    List<Build>  builds =job2.getAllBuilds();
                    System.err.println(builds);
                    job2 = job2.details();
                    job2.getUrl();
                    System.err.println(job2);
                }catch (Exception e){

                }

                x++;
                if (x > 100) break;
            }catch (IOException e){

            }

        }

    }

}
