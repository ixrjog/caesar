package com.baiyi.caesar.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/11/11 11:32 上午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class HotUser implements Serializable {

    private static final long serialVersionUID = 6293417977054171725L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "显示名")
    private String displayName;

    @ApiModelProperty(value = "构建总数")
    private int count;
}

