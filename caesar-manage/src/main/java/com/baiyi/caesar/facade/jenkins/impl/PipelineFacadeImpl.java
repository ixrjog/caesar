package com.baiyi.caesar.facade.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.facade.jenkins.JobEngineFacade;
import com.baiyi.caesar.facade.jenkins.PipelineFacade;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.baiyi.caesar.jenkins.handler.JenkinsBlueHandler;
import com.baiyi.caesar.jenkins.util.PipelineUtil;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/31 10:27 上午
 * @Version 1.0
 */
@Service
public class PipelineFacadeImpl implements PipelineFacade {

    public static final int MY_TASK_SIZE = 3;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private JenkinsBlueHandler jenkinsBlueHandler;

    @Resource
    private JobEngineFacade jobEngineFacade;

    @Override
    public List<JenkinsPipelineVO.Pipeline> queryBuildJobPipelines(String username) {
        List<CsCiJobBuild> builds = csCiJobBuildService.queryMyCiJobBuild(username, MY_TASK_SIZE);
        List<JenkinsPipelineVO.Pipeline> pipelines = Lists.newArrayList();
        for (CsCiJobBuild build : builds) {
            String serverName = jobEngineFacade.acqJenkinsServerName(build.getJobEngineId());
            List<PipelineNode> nodes;
            if (build.getFinalized()) {
                nodes = jenkinsBlueHandler.queryJobRunNodesByCache(serverName, build.getJobName(), build.getEngineBuildNumber());
            } else {
                nodes = jenkinsBlueHandler.queryJobRunNodes(serverName, build.getJobName(), build.getEngineBuildNumber());
            }
            JenkinsPipelineVO.Pipeline pipeline = JenkinsPipelineVO.Pipeline.builder()
                    .id(build.getId())
                    .nodes(PipelineUtil.convert(nodes))
                    .jobName(build.getJobName())
                    .jobBuildNumber(build.getJobBuildNumber())
                    .isRunning(!build.getFinalized())
                    .build();
            pipelines.add(pipeline);
        }
        return pipelines;
    }

    @Override
    public List<JenkinsPipelineVO.Pipeline> queryDeploymentJobPipelines(String username) {
        List<CsCdJobBuild> builds = csCdJobBuildService.queryMyCdJobBuild(username, MY_TASK_SIZE);
        List<JenkinsPipelineVO.Pipeline> pipelines = Lists.newArrayList();
        for (CsCdJobBuild build : builds) {
            String serverName = jobEngineFacade.acqJenkinsServerName(build.getJobEngineId());
            List<PipelineNode> nodes;
            if (build.getFinalized()) {
                nodes = jenkinsBlueHandler.queryJobRunNodesByCache(serverName, build.getJobName(), build.getEngineBuildNumber());
            } else {
                nodes = jenkinsBlueHandler.queryJobRunNodes(serverName, build.getJobName(), build.getEngineBuildNumber());
            }
            if (CollectionUtils.isEmpty(nodes)) continue;
            JenkinsPipelineVO.Pipeline pipeline = JenkinsPipelineVO.Pipeline.builder()
                    .id(build.getId())
                    .nodes(PipelineUtil.convert(nodes))
                    .jobName(build.getJobName())
                    .jobBuildNumber(build.getJobBuildNumber())
                    .isRunning(!build.getFinalized())
                    .build();
            pipelines.add(pipeline);
        }
        return pipelines;
    }


}
