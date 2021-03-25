package com.baiyi.caesar.domain.vo.application;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/29 11:34 上午
 * @Version 1.0
 */
public class JobEngineVO {

    public interface IJobEngine {

        void setJobEngine(JobEngineVO.JobEngine jobEngine);

        JobEngineVO.JobEngine getJobEngine();

        Integer getJobEngineId();
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobEngine {

        @ApiModelProperty(value = "jenkins实例详情")
        private JenkinsInstanceVO.Instance jenkinsInstance;

        @ApiModelProperty(value = "最后构建任务url")
        private String lastBuildUrl;

        @ApiModelProperty(value = "需要升级模版")
        private Boolean needUpgrade;

        private Integer id;
        private Integer buildType;
        private Integer jobId;
        private Integer jenkinsInstanceId;
        private String name;
        private String jobUrl;
        private Integer lastBuildNumber;
        private Integer nextBuildNumber;
        private Integer lastCompletedBuildNumber;
        private Integer lastSuccessfulBuildNumber;
        private Integer lastFailedBuildNumber;
        private Integer jobStatus;
        private Integer tplVersion;
        private String tplHash;
        private Date createTime;
        private Date updateTime;
        private String comment;
    }
}
