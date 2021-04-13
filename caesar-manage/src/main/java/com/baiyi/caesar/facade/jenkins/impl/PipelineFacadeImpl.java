package com.baiyi.caesar.facade.jenkins.impl;

import com.baiyi.caesar.common.util.TimeAgoUtil;
import com.baiyi.caesar.decorator.jenkins.base.BaseJenkinsDecorator;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.pipeline.PipelineNodeStepLogParam;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.facade.jenkins.JobEngineFacade;
import com.baiyi.caesar.facade.jenkins.PipelineFacade;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.baiyi.caesar.jenkins.api.model.PipelineStep;
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
public class PipelineFacadeImpl extends BaseJenkinsDecorator implements PipelineFacade {

    public static final int MAX_QUERY_SIZE = 4;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;


    @Resource
    private JenkinsBlueHandler jenkinsBlueHandler;

    @Resource
    private JobEngineFacade jobEngineFacade;

    private int buildQuerySize(Integer size) {
        if (size == null || size > MAX_QUERY_SIZE)
            return MAX_QUERY_SIZE;
        return size;
    }

    @Override
    public String queryPipelineNodeLog(PipelineNodeStepLogParam.PipelineNodeStepLogQuery query) {
        if (query.getBuildType() == 0) {
            CsCiJobBuild build = csCiJobBuildService.queryCiJobBuildById(query.getBuildId());
            String serverName = jobEngineFacade.acqJenkinsServerName(build.getJobEngineId());
            List<PipelineStep> steps = jenkinsBlueHandler.queryJobRunNodesSteps(serverName, build.getJobName(), build.getEngineBuildNumber(), query.getNodeId());
            if (CollectionUtils.isEmpty(steps)) return "";
            PipelineStep step = steps.get(0);
            return jenkinsBlueHandler.queryJobRunNodesStepLog(serverName, build.getJobName(), build.getEngineBuildNumber(), query.getNodeId(), step.getId());
        } else {
            CsCdJobBuild build = csCdJobBuildService.queryCdJobBuildById(query.getBuildId());
            String serverName = jobEngineFacade.acqJenkinsServerName(build.getJobEngineId());
            List<PipelineStep> steps = jenkinsBlueHandler.queryJobRunNodesSteps(serverName, build.getJobName(), build.getEngineBuildNumber(), query.getNodeId());
            if (CollectionUtils.isEmpty(steps)) return "";
            PipelineStep step = steps.get(0);
            return jenkinsBlueHandler.queryJobRunNodesStepLog(serverName, build.getJobName(), build.getEngineBuildNumber(), query.getNodeId(), step.getId());
        }

    }


    @Override
    public List<JenkinsPipelineVO.Pipeline> queryBuildJobPipelines(String username, Integer size) {
        List<CsCiJobBuild> builds = csCiJobBuildService.queryMyCiJobBuild(username, buildQuerySize(size));
        List<JenkinsPipelineVO.Pipeline> pipelines = Lists.newArrayList();
        for (CsCiJobBuild build : builds) {
            String serverName = jobEngineFacade.acqJenkinsServerName(build.getJobEngineId());
            List<PipelineNode> nodes;
            if (build.getFinalized()) {
                nodes = jenkinsBlueHandler.queryJobRunNodesByCache(serverName, build.getJobName(), build.getEngineBuildNumber());
            } else {
                nodes = jenkinsBlueHandler.queryJobRunNodes(serverName, build.getJobName(), build.getEngineBuildNumber());
            }
            if (CollectionUtils.isEmpty(nodes)) // 加入队列状态
                nodes = Lists.newArrayList(PipelineNode.QUEUE);
            JenkinsPipelineVO.Pipeline pipeline = JenkinsPipelineVO.Pipeline.builder()
                    .id(build.getId())
                    .nodes(PipelineUtil.convert(nodes))
                    .jobName(build.getJobName())
                    .jobBuildNumber(build.getJobBuildNumber())
                    .isRunning(!build.getFinalized())
                    .startTime(build.getStartTime())
                    .buildType(BuildType.BUILD.getType())
                    .build();
            setChartHeight(pipeline);
            decorator(pipeline);
            pipelines.add(pipeline);
        }
        return pipelines;
    }

    private void setChartHeight(JenkinsPipelineVO.Pipeline pipeline) {
        int size = 0;
        for (JenkinsPipelineVO.Node node : pipeline.getNodes()) {
            if (!CollectionUtils.isEmpty(node.getChildren())) {
                if (node.getChildren().size() > size)
                    size = node.getChildren().size();
            }
        }
        if (size > 1) {
            Integer h = 120 + 50 * (size - 1);
            pipeline.setChartHeight(h + "px");
        }
    }

    @Override
    public List<JenkinsPipelineVO.Pipeline> queryDeploymentJobPipelines(String username, Integer size) {
        List<CsCdJobBuild> builds = csCdJobBuildService.queryMyCdJobBuild(username, buildQuerySize(size));
        List<JenkinsPipelineVO.Pipeline> pipelines = Lists.newArrayList();
        for (CsCdJobBuild build : builds) {
            String serverName = jobEngineFacade.acqJenkinsServerName(build.getJobEngineId());
            List<PipelineNode> nodes;
            if (build.getFinalized()) {
                nodes = jenkinsBlueHandler.queryJobRunNodesByCache(serverName, build.getJobName(), build.getEngineBuildNumber());
            } else {
                nodes = jenkinsBlueHandler.queryJobRunNodes(serverName, build.getJobName(), build.getEngineBuildNumber());
            }
            if (CollectionUtils.isEmpty(nodes)) // 加入队列状态
                nodes = Lists.newArrayList(PipelineNode.QUEUE);
            JenkinsPipelineVO.Pipeline pipeline = JenkinsPipelineVO.Pipeline.builder()
                    .id(build.getId())
                    .nodes(PipelineUtil.convert(nodes))
                    .jobName(build.getJobName())
                    .jobBuildNumber(build.getJobBuildNumber())
                    .isRunning(!build.getFinalized())
                    .startTime(build.getStartTime())
                    .buildType(BuildType.DEPLOYMENT.getType())
                    .build();
            setChartHeight(pipeline);
            decorator(pipeline);
            pipelines.add(pipeline);
        }
        return pipelines;
    }

    private void decorator(JenkinsPipelineVO.Pipeline pipeline) {
        decoratorBuildExecutors(pipeline);
        TimeAgoUtil.decorator(pipeline);
    }


}
