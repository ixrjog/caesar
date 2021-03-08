package com.baiyi.caesar.domain.param.kubernetes;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:41 下午
 * @Version 1.0
 */
public class KubernetesApplicationParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "服务器组id", example = "1")
        private Integer serverGroupId;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;
    }
}
