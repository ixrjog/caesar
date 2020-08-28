package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildExecutor;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildExecutorService;
import com.baiyi.caesar.service.server.OcServerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/28 10:18 上午
 * @Version 1.0
 */
@Component
public class JobDeploymentDecorator {

    @Resource
    private CsCiJobBuildExecutorService csCiJobBuildExecutorService;

    @Resource
    private OcServerService ocServerService;

    public CdJobBuildVO.JobBuild decorator(CdJobBuildVO.JobBuild jobBuild, Integer extend) {
//        if (extend == 0) return jobBuild;
//        // 装饰工作引擎
//        CsCdJobEngine csCdJobEngine = csCdJobEngineService.queryCsCdJobEngineById(jobBuild.getJobEngineId());
//        if (csCdJobEngine != null) {
//            CdJobVO.JobEngine jobEngine = cdJobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCdJobEngine, CdJobVO.JobEngine.class));
//            jobBuild.setJobEngine(jobEngine);
//
//            String jobBuildUrl = Joiner.on("/").join(jobEngine.getJobUrl(), jobBuild.getEngineBuildNumber());
//            jobBuild.setJobBuildUrl(jobBuildUrl);
//        }
        return jobBuild;
    }

    public List<CiJobBuildVO.BuildExecutor> getBuildExecutorByBuildId(int buildId) {
        List<CsCiJobBuildExecutor> executors = csCiJobBuildExecutorService.queryCsCiJobBuildExecutorByBuildId(buildId);
        return executors.stream().map(e -> {
                    CiJobBuildVO.BuildExecutor buildExecutor = BeanCopierUtils.copyProperties(e, CiJobBuildVO.BuildExecutor.class);
                    OcServer ocServer = ocServerService.queryOcServerByIp(buildExecutor.getPrivateIp());
                    if (ocServer != null)
                        buildExecutor.setServer(BeanCopierUtils.copyProperties(ocServer, ServerVO.Server.class));
                    return buildExecutor;
                }
        ).collect(Collectors.toList());
    }
}
