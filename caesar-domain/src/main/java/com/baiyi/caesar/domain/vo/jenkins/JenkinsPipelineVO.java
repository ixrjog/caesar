package com.baiyi.caesar.domain.vo.jenkins;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/3/30 6:04 下午
 * @Version 1.0
 */
public class JenkinsPipelineVO {

    @Data
    @Builder
    @ApiModel
    public static class Node {

        private String name;
        private String state;
        @Builder.Default
        private Integer completePercent = 100;
        private String id;
        @Builder.Default
        private String type = "STAGE";

    }
}
