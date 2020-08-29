package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/28 9:48 上午
 * @Version 1.0
 */
@Data
@Builder
public class CdJobBuildBO {

    private Integer id;
    private Integer cdJobId;
    private Integer ciBuildId;
    private Integer jobEngineId;
    private Integer ciJobBuildId;
    private String jobName;
    private Integer applicationId;
    private String username;
    private Integer jobBuildNumber;
    private Integer engineBuildNumber;
    private String buildPhase;
    private String buildStatus;
    @Builder.Default
    private Date startTime;
    private Date endTime;
    @Builder.Default
    private Boolean finalized;
    private String versionName;
    private String versionDesc;
    private Date createTime;
    private Date updateTime;
    private String parameters;
    private String dingtalkMsg;
    private String comment;

}
