package com.baiyi.caesar.domain.param.auth;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


public class RoleParam {


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "角色名称")
        private String roleName;

        @ApiModelProperty(value = "资源名称")
        private String resourceName;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserTicketOcAuthRoleQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        private String username;

        @ApiModelProperty(value = "工单票据id")
        private Integer workorderTicketId;

    }


}
