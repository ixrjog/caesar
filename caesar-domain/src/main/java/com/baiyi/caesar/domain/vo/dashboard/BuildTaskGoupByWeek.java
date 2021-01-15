package com.baiyi.caesar.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/11/9 3:28 下午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class BuildTaskGoupByWeek implements Serializable {

    private static final long serialVersionUID = -1611295369631578171L;
    @ApiModelProperty(value = "年周%Y%u")
    private String weeks;

    @ApiModelProperty(value = "执行次数")
    private int count;
}