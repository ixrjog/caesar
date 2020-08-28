package com.baiyi.caesar.domain.param.jenkins;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/27 4:51 下午
 * @Version 1.0
 */
public class JobDeploymentParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DeploymentParam {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer ciJobId;

        @ApiModelProperty(value = "分支")
        private String branch;

        @ApiModelProperty(value = "版本名称")
        private String versionName;

        @ApiModelProperty(value = "版本说明")
        private String versionDesc;

        private Map<String, String> paramMap;

    }
}
