package com.baiyi.caesar.jenkins.util;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/30 5:59 下午
 * @Version 1.0
 */
public class PipelineUtil {


    public interface States {
        String FINISHED = "FINISHED";
        String RUNNING = "RUNNING";
        String SKIPPED = "SKIPPED";
        String PAUSED = "PAUSED";

    }

    public interface Results {
        String SUCCESS = "SUCCESS";
        String UNKNOWN = "UNKNOWN";
    }

    public static List<JenkinsPipelineVO.Node> convert(List<PipelineNode> nodes) {
        if(CollectionUtils.isEmpty(nodes))
            return Collections.EMPTY_LIST;
        return nodes.stream().map(e ->
                JenkinsPipelineVO.Node.builder()
                        .name(e.getDisplayName())
                        .id(e.getId())
                        .state(convertState(e))
                        .build()
        ).collect(Collectors.toList());
    }

    private static String convertState(PipelineNode node) {
        if (StringUtils.isEmpty(node.getState()))
            return "not_built";
        switch (node.getState()) {
            case States.FINISHED:
                return node.getResult();
            case States.RUNNING:
                return States.RUNNING;
            case States.SKIPPED:
                return States.SKIPPED;
            case States.PAUSED:
                return States.PAUSED;
            default:
                return "not_built";
        }

    }
}
