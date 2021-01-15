package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/9/11 5:49 下午
 * @Version 1.0
 */
@Data
@Builder
public class JobBuildServerBO {

    private Integer id;

    private Integer buildType;

    private Integer buildId;

    private Integer jobId;

    private String hostPattern;

    @Builder.Default
    private String source = "OPSCLOUD";

    private Integer serverId;

    private String versionName;

    private String serverName;

    private String privateIp;

    private Integer serialNumber;

    private Integer envType;

}
