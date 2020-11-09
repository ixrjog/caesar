package com.baiyi.caesar.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/11/9 11:14 上午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class JobTypeTotal implements Serializable {

    private static final long serialVersionUID = -2348783349464684355L;
    @ApiModelProperty(value = "任务类型")
    private String jobType;

    @ApiModelProperty(value = "总数")
    private int total;
}

