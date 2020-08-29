package com.baiyi.caesar.domain.vo.build;

import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/28 10:12 上午
 * @Version 1.0
 */
public class CdJobBuildVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuild {

        private JobEngineVO.JobEngine jobEngine;
        @ApiModelProperty(value = "产出物")
        private List<CiJobBuildVO.BuildArtifact> artifacts;

        @ApiModelProperty(value = "执行节点")
        private List<CiJobBuildVO.BuildExecutor> executors;

        private String jobBuildUrl;
        @ApiModelProperty(value = "构建用户")
        private UserVO.User user;
        @ApiModelProperty(value = "历时")
        private String ago;
        @ApiModelProperty(value = "构建时长")
        private String buildTime;

        private Integer id;
        private Integer cdJobId;
        private Integer ciBuildId;
        private Integer jobEngineId;
        private String jobName;
        private Integer applicationId;
        private String username;
        private Integer jobBuildNumber;
        private Integer engineBuildNumber;
        private String buildPhase;
        private String buildStatus;
        @ApiModelProperty(value = "开始时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @ApiModelProperty(value = "结束时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;
        private Boolean finalized;
        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
        private String parameters;
        private String dingtalkMsg;
        private String comment;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildArtifact {

        private String artifactFileSize; // 产出物文件容量
        private String ossUrl;

        private Integer id;
        private Integer buildId;
        private Integer ciJobId;
        private String jobName;
        private String artifactDisplayPath;
        private String artifactFileName;
        private String artifactRelativePath;
        private Long artifactSize;
        private String storagePath;
        private Date updateTime;
        private Date createTime;
    }

}
