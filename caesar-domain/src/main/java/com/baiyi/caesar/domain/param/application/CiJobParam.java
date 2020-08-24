package com.baiyi.caesar.domain.param.application;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/7/29 11:50 上午
 * @Version 1.0
 */
public class CiJobParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CiJobPageQuery extends PageParam {

        @ApiModelProperty(value = "应用id",example = "1")
        @NotNull(message = "必须指定应用id")
        private Integer applicationId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CiJobTplPageQuery extends PageParam {

        @ApiModelProperty(value = "模版id",example = "1")
        @NotNull(message = "必须模版id")
        private Integer jobTplId;

    }


}
