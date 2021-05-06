package com.baiyi.caesar.factory.jenkins.impl.deployment;

import com.baiyi.caesar.builder.jenkins.JobBuildServerBuilder;
import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.common.exception.build.BuildRuntimeException;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.common.util.RegexUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.server.ServerGroupHostPatternVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.facade.ServerGroupFacade;
import com.baiyi.caesar.facade.jenkins.JobFacade;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.factory.jenkins.builder.JenkinsJobParamsBuilder;
import com.baiyi.caesar.factory.jenkins.builder.JenkinsJobParamsMap;
import com.baiyi.caesar.factory.jenkins.monitor.MonitorHandler;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.jenkins.context.JobParametersContext;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import com.baiyi.caesar.util.JobParamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Build.*;
import static com.baiyi.caesar.dingtalk.impl.JavaDeploymentNotify.HOST_PATTERN;
import static com.baiyi.caesar.dingtalk.impl.JavaDeploymentNotify.SERVER_GROUP;

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
        return JobTypeEnum.JAVA_DEPLOYMENT.getType();
    }

    @Resource
    private JobFacade jobFacade;

    @Override
    protected boolean isLimitConcurrentJob() {
        return true;
    }

    @Resource
    private MonitorHandler monitorHandler;

    @Resource
    private CsApplicationServerGroupService csApplicationServerGroupService;

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Override
    protected void updateHostStatus(CsApplication csApplication, Map<String, String> params, int status) {
        monitorHandler.updateHostStatus(csApplication, params, status);
    }

    @Override
    protected JobParametersContext buildBaseBuildParams(CsApplication csApplication, CsCdJob csCdJob, JobDeploymentParam.DeploymentParam deploymentParam) {
        JobParametersContext jobParamDetail = super.buildBaseBuildParams(csApplication, csCdJob, deploymentParam);

        JenkinsJobParamsMap jenkinsJobParamsMap = JenkinsJobParamsBuilder.newBuilder()
                .paramEntry(HOST_PATTERN, deploymentParam)
                .paramEntry(JOB_BUILD_NUMBER, String.valueOf(csCdJob.getJobBuildNumber()))
                .paramEntry(CONCURRENT, deploymentParam.getParamMap().getOrDefault(CONCURRENT, "1"))
                .paramEntry(OSS_JOB_URL, JobParamUtils.getOssJobUrl(csCdJob.getJobBuildNumber(), jobParamDetail))
                .build();
        jobParamDetail.putParams(jenkinsJobParamsMap.getParams());
        return jobParamDetail;
    }

    /**
     * 校验参数
     * CsApplication csApplication, CsCiJob csCiJob, JenkinsJobParameters jenkinsJobParameters, JenkinsJobParamsMap jenkinsJobParamsMap
     * @param context
     */
    @Override
    protected void checkParameters(JobParametersContext context) {
        if (!context.getParams().containsKey(HOST_PATTERN))
            throw new BuildRuntimeException(ErrorEnum.JENKINS_PARAM_HOST_PATTERN_EMPTY);
        String hostPattern = context.getParams().get(HOST_PATTERN);
        String serverGroupName = queryServerGroupName(context.getJenkinsJobParameters().getParameters());
        authServerGroup(context.getApplication(), serverGroupName);

        BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> hostPatternWrapper
                = serverGroupFacade.queryServerGroupHostPattern(serverGroupName, context.getBuildJob().getEnvType());
        if (!hostPatternWrapper.isSuccess())
            throw new BuildRuntimeException(hostPatternWrapper.getCode(), hostPatternWrapper.getDesc());
        authHostPattern(hostPattern, hostPatternWrapper.getBody());
    }

    private void authHostPattern(String hostPattern, List<ServerGroupHostPatternVO.HostPattern> hostPatterns) {
        for (ServerGroupHostPatternVO.HostPattern hp : hostPatterns) {
            if (hp.getHostPattern().equals(hostPattern))
                return;
        }
        throw new BuildRuntimeException(ErrorEnum.JENKINS_PARAM_HOST_PATTERN_ERROR);
    }

    private void authServerGroup(CsApplication csApplication, String serverGroupName) {
        List<CsApplicationServerGroup> serverGroups = csApplicationServerGroupService.queryCsApplicationServerGroupByApplicationId(csApplication.getId());
        for (CsApplicationServerGroup serverGroup : serverGroups) {
            if (serverGroup.getServerGroupName().equals(serverGroupName))
                return;
        }
        throw new BuildRuntimeException(ErrorEnum.APPLICATION_SERVERGROUP_NON_COMPLIANCE);
    }


    private String queryServerGroupName(List<JenkinsJobParameters.Parameter> parameters) {
        for (JenkinsJobParameters.Parameter parameter : parameters) {
            if (parameter.getName().equals(SERVER_GROUP))
                return parameter.getValue();
        }
        throw new BuildRuntimeException(ErrorEnum.JENKINS_PARAM_SERVER_GROUP_EMPTY);
    }

    @Override
    protected List<CsJobBuildArtifact> filterBuildArtifacts(List<CsJobBuildArtifact> artifacts) {
        return artifacts.stream().filter(e -> RegexUtil.checkJar(e.getArtifactFileName())).collect(Collectors.toList());
    }

    @Override
    protected void saveDetails(DeploymentJobContext context) {
        BusinessWrapper<List<ServerGroupHostPatternVO.HostPattern>> wrapper = jobFacade.queryCdJobHostPatternByJobId(context.getCsCdJob().getId());
        if (!wrapper.isSuccess()) return;
        JobParametersContext jobParamDetail = context.getJobParamDetail();
        String hostPattern = jobParamDetail.getParams().get(HOST_PATTERN);
        wrapper.getBody().parallelStream().forEach(e -> {
            if (e.getHostPattern().equals(hostPattern)) {
                List<ServerVO.Server> servers = e.getServers();
                servers.parallelStream().forEach(server ->
                        csJobBuildServerService.addCsJobBuildServer(JobBuildServerBuilder.build(context, hostPattern, server))
                );
            }
        });
    }

}