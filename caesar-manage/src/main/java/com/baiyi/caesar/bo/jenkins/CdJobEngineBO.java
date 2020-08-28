package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/27 5:46 下午
 * @Version 1.0
 */
@Data
@Builder
public class CdJobEngineBO {

    private Integer id;
    private Integer cdJobId;
    private Integer jenkinsInstanceId;
    private String name;
    private String jobUrl;
    @Builder.Default
    private Integer lastBuildNumber = 0;
    @Builder.Default
    private Integer nextBuildNumber = 1;
    @Builder.Default
    private Integer lastCompletedBuildNumber = 0;
    @Builder.Default
    private Integer lastSuccessfulBuildNumber = 0;
    @Builder.Default
    private Integer lastFailedBuildNumber = 0;
    @Builder.Default
    private Integer jobStatus = 0;
    private Integer tplVersion;
    private String tplHash;
    private Date createTime;
    private Date updateTime;
    private String comment;
}
