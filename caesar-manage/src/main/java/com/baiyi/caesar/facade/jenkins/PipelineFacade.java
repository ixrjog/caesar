package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/31 10:19 上午
 * @Version 1.0
 */
public interface PipelineFacade {

    List<JenkinsPipelineVO.Pipeline> queryBuildJobPipelines(String username,Integer size);

    List<JenkinsPipelineVO.Pipeline> queryDeploymentJobPipelines(String username,Integer size);
}
