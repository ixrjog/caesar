package com.baiyi.caesar.domain.param.pipeline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/4/13 3:00 下午
 * @Version 1.0
 */
public class PipelineNodeStepLogParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PipelineNodeStepLogQuery  {

        @ApiModelProperty(value = "构建类型")
        private Integer buildType;

        @ApiModelProperty(value = "构建id",example = "1")
        private Integer buildId;

        @ApiModelProperty(value = "节点id", example = "1")
        private Integer nodeId;

    }
}
