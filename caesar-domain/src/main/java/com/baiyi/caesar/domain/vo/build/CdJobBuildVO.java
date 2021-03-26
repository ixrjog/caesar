package com.baiyi.caesar.domain.vo.build;

import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.base.AgoVO;
import com.baiyi.caesar.domain.vo.base.BuildTimeVO;
import com.baiyi.caesar.domain.vo.base.JobBuildVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/28 10:12 上午
 * @Version 1.0
 */
public class CdJobBuildVO {

    public interface IBuildView {

        Integer getCdJobId();

        void setBuildViews(List<BuildViewVO.JobBuildView> buildViews);
    }

    public interface IDeploymentServers {

        void setServers(List<DeploymentServerVO.BuildServer> servers);

        Integer getBuildId();
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobBuild implements UserVO.IUser, AgoVO.IAgo, BuildTimeVO.IBuildTime, BuildArtifactVO.IBuildArtifacts, JobEngineVO.IJobEngine,
            BuildExecutorVO.IBuildExecutors, JobBuildVO.IJobBuild, IDeploymentServers, Serializable {

        private static final long serialVersionUID = 1322824272757498254L;

        @Override
        public Integer getBuildId() {
            return this.id;
        }

        @Override
        public void setNoArtifact(Boolean noArtifact) {
        }

        @Override
        public int getBuildType() {
            return BuildType.DEPLOYMENT.getType();
        }

        @Override
        public Date getAgoTime() {
            return this.startTime;
        }

        private JobEngineVO.JobEngine jobEngine;
        @ApiModelProperty(value = "产出物")
        private List<BuildArtifactVO.BuildArtifact> artifacts;

        @ApiModelProperty(value = "执行节点")
        private List<BuildExecutorVO.BuildExecutor> executors;

        @ApiModelProperty(value = "部署服务器")
        private List<DeploymentServerVO.BuildServer> servers;

        private String hostPattern;

        public void setServers(List<DeploymentServerVO.BuildServer> servers) {
            this.servers = servers;
            if (!CollectionUtils.isEmpty(servers))
                this.hostPattern = servers.get(0).getHostPattern();
        }

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


}
