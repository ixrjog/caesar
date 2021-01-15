package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.baiyi.caesar.builder.jenkins.JobBuildServerBuilder;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.util.RegexUtils;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.server.ServerGroupHostPatternVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.factory.jenkins.monitor.MonitorHandler;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.context.JobParamDetail;
import com.baiyi.caesar.util.JobParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.dingtalk.impl.JavaDeploymentNotify.HOST_PATTERN;

/**
 * @Author baiyi
 * @Date 2020/9/10 4:54 下午
 * @Version 1.0
 */
@Slf4j
@Component("JavaDeployJobHandler")
public class JavaDeployJobHandler extends BaseDeploymentJobHandler implements IDeploymentJobHandler {

    @Override
    public String getKey() {
        return JobType.JAVA_DEPLOYMENT.getType();
    }

    @Resource
    private JobFacade jobFacade;

    @Override
    protected boolean isLimitConcurrentJob() {
        return true;
    }

    @Resource
    private MonitorHandler monitorHandler;

    @Override
    protected void updateHostStatus(CsApplication csApplication, Map<String, String> params, int status) {
        monitorHandler.updateHostStatus(csApplication, params, status);
    }

    @Override
    protected JobParamDetail acqBaseBuildParams(CsApplication csApplication, CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        JobParamDetail jobParamDetail = super.acqBaseBuildParams(csApplication, csCdJob, deploymentParam);
        JobParamUtils.assembleJobBuildNumberParam(csCdJob, jobParamDetail);
        JobParamUtils.assembleOssJobUrlParam(csCdJob, jobParamDetail);
        JobParamUtils.assembleHostPatternParam(jobParamDetail, deploymentParam);
        JobParamUtils.assembleConcurrentParam(jobParamDetail, deploymentParam);
        return jobParamDetail;
    }

    @Override
    protected List<CsJobBuildArtifact> filterBuildArtifacts(List<CsJobBuildArtifact> artifacts) {
        return artifacts.stream().filter(e -> RegexUtils.checkJar(e.getArtifactFileName())).collect(Collectors.toList());
    }

    @Override
    protected void saveDetails(DeploymentJobContext context) {
        BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> wrapper = jobFacade.queryCdJobHostPatternByJobId(context.getCsCdJob().getId());
        if (!wrapper.isSuccess()) return;
        JobParamDetail jobParamDetail = context.getJobParamDetail();
        String hostPattern = jobParamDetail.getParams().get(HOST_PATTERN);
        wrapper.getBody().forEach(e -> {
            if (e.getHostPattern().equals(hostPattern)) {
                List<ServerVO.Server> servers = e.getServers();
                servers.forEach(s ->
                        csJobBuildServerService.addCsJobBuildServer(JobBuildServerBuilder.build(context, hostPattern, s))
                );
            }
        });
    }

}