package com.baiyi.caesar.domain.vo.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/8 5:14 下午
 * @Version 1.0
 */
public class ServerGroupHostPatternVO {

    @Builder
    @Data
    @ApiModel
    public static class HostPattern {

        @ApiModelProperty(value = "主机模式（主机分组）")
        private String hostPattern;

        @ApiModelProperty(value = "自动选中")
        private Boolean isSelected;

        @ApiModelProperty(value = "主分组")
        private Boolean isMaster;

        @ApiModelProperty(value = "主机详情")
        private List<ServerVO.Server> servers;
    }
}
