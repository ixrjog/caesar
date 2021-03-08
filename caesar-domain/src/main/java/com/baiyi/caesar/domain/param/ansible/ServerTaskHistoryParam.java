package com.baiyi.caesar.domain.param.ansible;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/5 11:22 上午
 * @Version 1.0
 */
public class ServerTaskHistoryParam {


    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "任务类型")
        private Integer taskType;
    }

}
