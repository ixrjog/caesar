package com.baiyi.caesar.domain.param.dingtalk;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:27 下午
 * @Version 1.0
 */
public class DingtalkParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class DingtalkPageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;


        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;
    }
}
