package com.baiyi.caesar.domain.param.aliyun;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 3:28 下午
 * @Since 1.0
 */
public class CrParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class InstancePageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询专用")
        private String queryName;
        @ApiModelProperty(value = "是否有效")
        private Boolean isActive;
        private Integer extend;

    }
}
