package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:32 下午
 * @Version 1.0
 */
@Data
@Builder
public class CiJobBuildBO {

    private Integer id;
    private Integer ciJobId;
    private Integer jobEngineId;
    private String jobName;
    private Integer applicationId;
    private String branch;
    private String username;
    private Integer jobBuildNumber;
    private Integer engineBuildNumber;
    private String versionName;
    private String versionDesc;
    private String commit;
    private String buildPhase;
    private String buildStatus;
    @Builder.Default
    private Date startTime = new Date();
    private Date endTime;
    @Builder.Default
    private Boolean finalized = false;
    private Date createTime;
    private Date updateTime;
    private String parameters;
    private String dingtalkMsg;
    private Boolean isSilence;
    private String comment;
}
