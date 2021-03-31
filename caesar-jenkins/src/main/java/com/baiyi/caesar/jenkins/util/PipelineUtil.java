package com.baiyi.caesar.jenkins.util;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/30 5:59 下午
 * @Version 1.0
 */
public class PipelineUtil {

    public static List<JenkinsPipelineVO.Node> convert(List<PipelineNode> nodes) {
        return nodes.stream().map(e ->
                JenkinsPipelineVO.Node.builder()
                        .name(e.getDisplayName())
                        .id(e.getId())
                        .state(e.getState())
                        .build()
        ).collect(Collectors.toList());
    }
}
