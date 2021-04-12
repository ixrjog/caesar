package com.baiyi.caesar.domain.param.jenkins;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class BuildPageQuery extends PageParam {

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
    public static class JobBuildArtifactQuery {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer ciJobId;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuildQuery {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer buildId;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildParam {

        @ApiModelProperty(value = "任务id")
        @NotNull
        private Integer ciJobId;

        @ApiModelProperty(value = "分支")
        @NotNull
        private String branch;

        @ApiModelProperty(value = "版本名称")
        private String versionName;

        @ApiModelProperty(value = "版本说明")
        private String versionDesc;

        @ApiModelProperty(value = "通知静默")
        private Boolean isSilence;

        @ApiModelProperty(value = "回滚操作")
        private Boolean isRollback;

        @ApiModelProperty(value = "质量管理")
        private Boolean isSonar;

        @ApiModelProperty(value = "自定义参数")
        private Map<String, String> paramMap;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ViewJobBuildOutputQuery {

        @ApiModelProperty(value = "构建类型")
        @NotNull(message = "构建类型不能为空")
        private Integer buildType;

        @ApiModelProperty(value = "构建id")
        @NotNull(message = "构建id不能为空")
        private Integer buildId;

    }
}
