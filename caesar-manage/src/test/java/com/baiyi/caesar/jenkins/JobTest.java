package com.baiyi.caesar.jenkins;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.RegexUtils;
import com.baiyi.caesar.common.util.TimeUtils;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.tree.EngineVO;
import com.baiyi.caesar.facade.jenkins.JenkinsEngineFacade;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.ComputerWithDetails;
import com.offbytwo.jenkins.model.Executor;
import com.offbytwo.jenkins.model.Job;
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
    private CsJobTplService csJobTplService;

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private JenkinsEngineFacade jenkinsEngineFacade;

    @Test
    void testBuild() {
        JobBuildParam.BuildParam buildParam = new JobBuildParam.BuildParam();
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
    void testTpl() {
        CsJobTpl csJobTpl = csJobTplService.queryCsJobTplById(9);
        try {
            jenkinsServerHandler.updateJob("master-1", "CANNON_cannon-server-prod", csJobTpl.getTplContent());
            jenkinsServerHandler.updateJob("master-2", "CANNON_cannon-server-prod", csJobTpl.getTplContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTrackJobBuildTask() {
        jobFacade.trackJobBuildTask();
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
                            System.err.println(computerWithDetails);
                            System.err.println(job);
                        }
                    }
                } catch (IOException e) {
                }
            }
        });

        System.err.println(computerMap);
    }

    @Test
    void testApk() {
        String fileName = "bm_debug_[ceshi].apk";
        System.err.println(RegexUtils.checkApk(fileName));
    }

    @Test
    void testBuildEngineChart() {
        EngineVO.Children chart = jenkinsEngineFacade.buildEngineChart();
        System.err.println(JSON.toJSONString(chart));
    }


    @Test
    void testJobDelete() {
        BusinessWrapper<Boolean> wrapper = jobFacade.deleteBuildJob(62);
        System.err.println(wrapper.isSuccess());

    }

}
