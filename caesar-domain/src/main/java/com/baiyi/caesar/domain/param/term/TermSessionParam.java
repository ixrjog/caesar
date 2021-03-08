package com.baiyi.caesar.domain.param.term;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:14 下午
 * @Version 1.0
 */
public class TermSessionParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        private Integer extend;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "会话是否关闭")
        private Boolean isClosed;

    }
}
