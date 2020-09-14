package com.baiyi.caesar.domain.vo.build;

import com.baiyi.caesar.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/9/14 11:50 上午
 * @Version 1.0
 */
public class DeploymentServerVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildServer {
        private ServerVO.Server server;
        private Integer id;
        private Integer buildType;
        private Integer buildId;
        private Integer jobId;
        private String hostPattern;
        private String source;
        private Integer serverId;
        private String versionName;
        private String serverName;
        private String privateIp;
        private Integer serialNumber;
        private Integer envType;

    }
}
