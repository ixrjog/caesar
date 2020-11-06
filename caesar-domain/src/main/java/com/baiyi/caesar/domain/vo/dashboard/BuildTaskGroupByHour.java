package com.baiyi.caesar.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/11/6 4:46 下午
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel
public class BuildTaskGroupByHour implements Serializable {

    private static final long serialVersionUID = 1527512946198364326L;
    @ApiModelProperty(value = "小时 00-23")
    private int hours;

    @ApiModelProperty(value = "任务次数")
    private int count;
}
