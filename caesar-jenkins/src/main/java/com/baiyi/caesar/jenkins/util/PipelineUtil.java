package com.baiyi.caesar.jenkins.util;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    private static Map<String, List<PipelineNode>> convertMap(List<PipelineNode> nodes) {
        Map<String, List<PipelineNode>> nodeMap = Maps.newHashMap();
        for (PipelineNode node : nodes) {
            if (node.getFirstParent() == null) {
                node.setFirstParent("0");
            }
            if (nodeMap.containsKey(node.getFirstParent())) {
                nodeMap.get(node.getFirstParent()).add(node);
            } else {
                nodeMap.put(node.getFirstParent(), Lists.newArrayList(node));
            }
        }
        return nodeMap;
    }

    public static List<JenkinsPipelineVO.Node> convert(List<PipelineNode> nodes) {
        if (CollectionUtils.isEmpty(nodes))
            return Collections.EMPTY_LIST;

        List<JenkinsPipelineVO.Node> result = Lists.newArrayList();
        for (PipelineNode pn : nodes) {
            JenkinsPipelineVO.Node node = JenkinsPipelineVO.Node.builder()
                    .firstParent(pn.getFirstParent())
                    .name(pn.getDisplayName())
                    .id(pn.getId())
                    .state(convertState(pn))
                    .build();
            if ("PARALLEL".equalsIgnoreCase(pn.getType())) {
                addChildren(result, node);
            } else {
                result.add(node);
            }
        }
        return result;
    }

    private static void addChildren(List<JenkinsPipelineVO.Node> result, JenkinsPipelineVO.Node node) {
        for (JenkinsPipelineVO.Node n : result) {
            if (n.getId().equalsIgnoreCase(node.getFirstParent())) {
                n.getChildren().add(node);
                return;
            }
        }

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
                return States.SKIPPED.toLowerCase();
            case States.PAUSED:
                return States.PAUSED;
            default:
                return "not_built";
        }

    }
}
