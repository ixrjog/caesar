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
 * @Date 2020/8/5 10:02 上午
 * @Version 1.0
 */
public class JobBuildParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuildPageQuery extends PageParam {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer ciJobId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }



    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CiBuildParam {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer ciJobId;

        @ApiModelProperty(value = "分支")
        private String branch;

        @ApiModelProperty(value = "版本名称")
        private String versionName;

        @ApiModelProperty(value = "版本说明")
        private String versionDesc;

        private Map<String,String> paramMap;

    }
}
