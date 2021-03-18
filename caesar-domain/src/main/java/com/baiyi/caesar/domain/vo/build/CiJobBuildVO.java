package com.baiyi.caesar.domain.vo.build;


import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/10 9:59 上午
 * @Version 1.0
 */
public class CiJobBuildVO {

    public interface IBuildView {
        Integer getCiJobId();
        void setBuildViews(List<BuildViewVO.JobBuildView> buildViews);
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuild implements Serializable {

        private static final long serialVersionUID = -448531182053453517L;

        private JobEngineVO.JobEngine jobEngine;
        @ApiModelProperty(value = "产出物")
        private List<BuildArtifactVO.BuildArtifact> artifacts;

        private Boolean noArtifact; // 没有构件（前端下拉列表禁止选中）

        @ApiModelProperty(value = "变更记录")
        private List<BuildChange> changes;
        @ApiModelProperty(value = "执行节点")
        private List<BuildExecutorVO.BuildExecutor> executors;

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
        @ApiModelProperty(value = "支持回滚版本")
        private Boolean supportRollback;
        private String versionName;
        private String versionDesc;

        @ApiModelProperty(value = "commit详情")
        private BaseCommit commitDetails;

        private String commit;
        private String buildPhase;
        private String buildStatus;
        private String operationUsername;
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
        private Boolean isSilence;
        private Boolean isRollback;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BaseCommit {
        private String commit;
        private String commitUrl;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildChange extends BaseCommit {
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
        // private String commit;
        private String commitMsg;
        // private String commitUrl;
    }

}
