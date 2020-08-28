package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/28 11:17 上午
 * @Version 1.0
 */
@Data
@Builder
public class CdJobBuildArtifactBO {

    private Integer id;
    private Integer buildId;
    private Integer cdJobId;
    private String jobName;
    private String artifactDisplayPath;
    private String artifactFileName;
    private String artifactRelativePath;
    private Long artifactSize;
    private String storagePath;
    private Date updateTime;
    private Date createTime;
}
