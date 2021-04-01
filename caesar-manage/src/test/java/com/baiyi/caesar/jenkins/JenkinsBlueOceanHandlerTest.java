package com.baiyi.caesar.jenkins;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.TimeUtil;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.baiyi.caesar.jenkins.handler.JenkinsBlueHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/23 2:36 下午
 * @Version 1.0
 */
public class JenkinsBlueOceanHandlerTest extends BaseUnit {

    @Resource
    private JenkinsBlueHandler jenkinsBlueOceanHandler;

    @Test
    void pipelineNodeTest() {
        List<PipelineNode> nodes = jenkinsBlueOceanHandler.queryJobRunNodes("master-1", "ACCOUNT_account-server-build-prod", 1);
        nodes.forEach(e -> {
            System.err.println(JSON.toJSON(e));
        });
    }

    @Test
    void timeTest() {
        Date d = TimeUtil.convertTime("2021-03-01T14:40:20.754+0800", TimeUtil.ISO8601);
        System.err.println(JSON.toJSON(d));
    }


}
