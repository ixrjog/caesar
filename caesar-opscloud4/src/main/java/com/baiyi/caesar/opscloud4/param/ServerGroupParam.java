package com.baiyi.caesar.opscloud4.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class ServerGroupEnvHostPatternQuery {

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "服务器组名称")
        @NotBlank
        private String serverGroupName;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class ServerGroupPageQuery  {

        @ApiModelProperty(value = "组名")
        private String name;

        @ApiModelProperty(value = "组类型")
        private Integer serverGroupTypeId;

        @ApiModelProperty(value = "分页页码")
        private Integer page;

        @ApiModelProperty(value = "分页页长",example = "10")
        private Integer length;

        private Boolean extend;

    }


}
