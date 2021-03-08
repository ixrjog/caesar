package com.baiyi.caesar.domain.param.ansible;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/13 4:46 下午
 * @Version 1.0
 */
public class AnsiblePlaybookParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询专用")
        private String queryName;

        @ApiModelProperty(value = "使用类型", example = "0")
        private Integer useType;
    }
}
