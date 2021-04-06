package com.baiyi.caesar.facade.jenkins;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/31 11:42 上午
 * @Version 1.0
 */
class PipelineFacadeTest extends BaseUnit {

    @Resource
    private PipelineFacade pipelineFacade;

    @Test
    void test() {
        List<JenkinsPipelineVO.Pipeline> pipelines = pipelineFacade.queryBuildJobPipelines("baiyi",3);
        System.err.println(JSON.toJSON(pipelines));
    }

}