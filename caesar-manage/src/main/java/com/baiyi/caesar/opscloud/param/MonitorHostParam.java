package com.baiyi.caesar.opscloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:35 上午
 * @Version 1.0
 */
public class MonitorHostParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MassUpdateMonitorHostStatus {

        @ApiModelProperty(value = "服务器组名称")
        @NotBlank
        private String serverGroupName;

        @ApiModelProperty(value = "服务器分组")
        @NotBlank
        private String hostPattern;


        @ApiModelProperty(value = "主机监控状态 0或1")
        @Max(value = 1, message = "监控状态必须为0或1")
        @Min(value = 0, message = "监控状态必须为0或1")
        private Integer status;

    }

}
