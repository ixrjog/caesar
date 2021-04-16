package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.param.pipeline.PipelineNodeStepLogParam;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.jenkins.api.model.PipelineStep;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/31 10:19 上午
 * @Version 1.0
 */
public interface PipelineFacade {

    String queryPipelineNodeLog(PipelineNodeStepLogParam.PipelineNodeStepLogQuery query);

    List<PipelineStep> queryPipelineNodeSteps(PipelineNodeStepLogParam.PipelineNodeStepLogQuery query);

    List<JenkinsPipelineVO.Pipeline> queryBuildJobPipelines(String username, Integer size);

    List<JenkinsPipelineVO.Pipeline> queryDeploymentJobPipelines(String username, Integer size);


}
