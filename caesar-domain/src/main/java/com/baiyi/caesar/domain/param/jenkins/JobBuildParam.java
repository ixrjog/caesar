package com.baiyi.caesar.domain.param.jenkins;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/5 10:02 上午
 * @Version 1.0
 */
public class JobBuildParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CiJobBuild {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer ciJobId;

        @ApiModelProperty(value = "分支")
        private String branch;

        private Map<String,String> paramMap;

    }
}
