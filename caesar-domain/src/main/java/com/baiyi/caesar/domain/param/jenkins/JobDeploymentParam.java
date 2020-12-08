package com.baiyi.caesar.domain.param.jenkins;

import com.baiyi.caesar.domain.param.PageParam;
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
    public static class DeploymentPageQuery extends PageParam {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer cdJobId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DeploymentParam {

        @ApiModelProperty(value = "任务id")
        @NotNull(message = "任务id不能为空")
        private Integer cdJobId;

        @ApiModelProperty(value = "构建任务id")
        @NotNull(message = "必须指定构件")
        private Integer ciBuildId;

        @ApiModelProperty(value = "版本名称")
        private String versionName;

        @ApiModelProperty(value = "版本说明")
        private String versionDesc;

        private Map<String, String> paramMap;

    }
}
