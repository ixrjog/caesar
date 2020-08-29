package com.baiyi.caesar.domain.vo.build;


import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
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
 * @Date 2020/8/10 9:59 上午
 * @Version 1.0
 */
public class CiJobBuildVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuild {

        private JobEngineVO.JobEngine jobEngine;
        @ApiModelProperty(value = "产出物")
        private List<BuildArtifact> artifacts;
        @ApiModelProperty(value = "变更记录")
        private List<BuildChange> changes;
        @ApiModelProperty(value = "执行节点")
        private List<CiJobBuildVO.BuildExecutor> executors;

        private String jobBuildUrl;
        @ApiModelProperty(value = "构建用户")
        private UserVO.User user;
        @ApiModelProperty(value = "历时")
        private String ago;
        @ApiModelProperty(value = "构建时长")
        private String buildTime;
        @ApiModelProperty(value = "7位commitId")
        private String shortCommitId;

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

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildChange {
        @ApiModelProperty(value = "7位commitId")
        private String shortCommitId;

        private Integer id;
        private Integer buildType;
        private Integer buildId;
        private Integer jobId;
        private String jobName;
        private String commitId;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date commitDate;
        private String authorFullName;
        private String authorAbsoluteUrl;
        private Date updateTime;
        private Date createTime;
        private String commit;
        private String commitMsg;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildExecutor {
        private ServerVO.Server server;

        private Integer id;
        private Integer buildId;
        private Integer ciJobId;
        private String jobName;
        private String nodeName;
        private String privateIp;
        private String rootDirectory;
        private String workspace;
        private String jobUrl;
        private Integer buildNumber;
        private Date createTime;
        private Date updateTime;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuildView {

        private List<CiJobBuildVO.BuildExecutor> executors;

        private String color;
        private Integer buildNumber;
        private Boolean building;
    }

}
