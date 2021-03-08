package com.baiyi.caesar.domain.param.aliyun;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:17 下午
 * @Version 1.0
 */
public class OSSBucketParam {


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class BucketPageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询专用")
        private String queryName;
        @ApiModelProperty(value = "是否有效")
        private Boolean isActive;
        private Integer extend;

    }
}
