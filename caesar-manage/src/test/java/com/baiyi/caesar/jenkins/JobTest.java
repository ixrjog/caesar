package com.baiyi.caesar.jenkins;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.TimeUtils;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.offbytwo.jenkins.model.*;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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


    @Test
    void testJenkins() {
        Map<String, Computer> computerMap = jenkinsServerHandler.getComputerMap("master-2");
        computerMap.keySet().forEach(k -> {
            if (!k.equals("master")) {
                Computer c = computerMap.get(k);
                try {
                    ComputerWithDetails computerWithDetails = c.details();
                    System.err.println(computerWithDetails);

                    List<Executor> executors = computerWithDetails.getExecutors();
                    for (Executor executor : executors) {
                        if (executor.getCurrentExecutable() != null) {
                            Job job = executor.getCurrentExecutable();
                            System.err.println(computerWithDetails );
                            System.err.println(job);
                        }
                    }
                } catch (IOException e) {
                }
            }


        });

        System.err.println(computerMap);
    }

}
