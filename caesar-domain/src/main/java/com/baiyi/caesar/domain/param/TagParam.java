package com.baiyi.caesar.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/3/8 1:29 下午
 * @Version 1.0
 */
@Data
@ApiModel
public class TagParam extends PageParam {

    @ApiModelProperty(value = "标签id")
    private Integer tagId;
}
