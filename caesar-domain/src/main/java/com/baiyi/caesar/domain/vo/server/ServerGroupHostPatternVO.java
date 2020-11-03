package com.baiyi.caesar.domain.vo.server;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/8 5:14 下午
 * @Version 1.0
 */
public class ServerGroupHostPatternVO {

    @Builder
    @Data
    @ApiModel
    public static class HostPattern {
        private String hostPattern;
        private List<ServerVO.Server> servers;
    }
}
