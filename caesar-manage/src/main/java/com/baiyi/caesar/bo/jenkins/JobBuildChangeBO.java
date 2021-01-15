package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/10 4:57 下午
 * @Version 1.0
 */
@Data
@Builder
public class JobBuildChangeBO {

    private Integer id;
    private Integer buildType;
    private Integer buildId;
    private Integer jobId;
    private String jobName;
    private String commitId;
    private Date commitDate;
    private String authorFullName;
    private String authorAbsoluteUrl;
    private Date updateTime;
    private Date createTime;
    private String commit;
    private String commitMsg;
}
