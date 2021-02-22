package com.baiyi.caesar.domain.param.gitlab;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:40 下午
 * @Version 1.0
 */
public class GitlabGroupParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GitlabGroupPageQuery extends GitlabProjectParam.GitlabProjectPageQuery {
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AddMember {

        @ApiModelProperty(value = "实例id", example = "1")
        @NotNull
        private Integer instanceId;

        @ApiModelProperty(value = "群组id", example = "1")
        @NotNull
        private Integer groupId;

        @ApiModelProperty(value = "用户名")
        @NotBlank
        private String username;

        @ApiModelProperty(value = "访问级别(GitlabAccessLevel)", example = "10")
        @NotNull
        private Integer accessLevel;

    }
}
