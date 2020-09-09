package com.baiyi.caesar.domain.vo.server;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/9/8 5:14 下午
 * @Version 1.0
 */
public class ServerGroupHostPatternVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class HostPattern {
        private String hostPattern;
    }
}
