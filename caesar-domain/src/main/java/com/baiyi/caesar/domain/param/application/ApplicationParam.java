package com.baiyi.caesar.domain.param.application;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/7/21 2:54 下午
 * @Version 1.0
 */
public class ApplicationParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApplicationPageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MyApplicationPageQuery extends PageParam {

        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ScmMemberBranchQuery {

        @ApiModelProperty(value = "scm成员id")
        @NotNull
        private Integer scmMemberId;

        @ApiModelProperty(value = "启用tag")
        @NotNull
        private Boolean enableTag;

    }
}
