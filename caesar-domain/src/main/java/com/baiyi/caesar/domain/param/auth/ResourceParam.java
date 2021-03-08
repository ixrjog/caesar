package com.baiyi.caesar.domain.param.auth;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/14 9:56 下午
 * @Version 1.0
 */
public class ResourceParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "资源组id")
        private Integer groupId;

        @ApiModelProperty(value = "资源路径")
        private String resourceName;

        @ApiModelProperty(value = "鉴权")
        private Integer needAuth;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class BindResourcePageQuery extends PageParam {

        @ApiModelProperty(value = "资源组id")
        private Integer groupId;

        @ApiModelProperty(value = "资源id")
        private Integer roleId;

    }
}
