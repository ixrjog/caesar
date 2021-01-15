package com.baiyi.caesar.domain.vo.application;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/9/10 1:38 下午
 * @Version 1.0
 */
public class ApplicationServerGroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApplicationServerGroup {

        private Integer id;
        @NotNull(message = "应用id不能为空")
        private Integer applicationId;
        @NotNull(message = "数据源不能为空")
        private String source;
        @NotNull(message = "服务器组id不能为空")
        private Integer serverGroupId;
        private String serverGroupName;
        private Date createTime;
        private Date updateTime;
        private String comment;

    }
}
