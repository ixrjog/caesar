package com.baiyi.caesar.service;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/11/2 8:00 下午
 * @Version 1.0
 */
public class CsCdJobBuildServiceTest extends BaseUnit {

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Test
    void test1(){
      //  csCdJobBuildService.queryLastCdJobBuild(18);
        JobDeploymentParam.DeploymentPageQuery pageQuery = new JobDeploymentParam.DeploymentPageQuery();
        pageQuery.setCdJobId(18);
        pageQuery.setPage(1);
        pageQuery.setLength(3);

        csCdJobBuildService.queryCdJobBuildPage( pageQuery);

        csCdJobBuildService.queryLastCdJobBuild(18);

    }
}
