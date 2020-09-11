package com.baiyi.caesar.opscloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupHostPatternQuery {

        @ApiModelProperty(value = "服务器组名")
        private String serverGroupName;

    }


}
