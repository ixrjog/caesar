package com.baiyi.caesar.domain.vo.application;

import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/27 5:37 下午
 * @Version 1.0
 */
public class CdJobVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CdJob {

        @ApiModelProperty(value = "环境详情")
        private EnvVO.Env env;

        @ApiModelProperty(value = "任务模版")
        private JobTplVO.JobTpl jobTpl;

        @ApiModelProperty(value = "构建视图")
        private List<CiJobBuildVO.JobBuildView> buildViews;

        @ApiModelProperty(value = "任务参数Map")
        private Map<String, String> parameters;

        @ApiModelProperty(value = "任务引擎")
        private List<CiJobVO.JobEngine> jobEngines;

        @ApiModelProperty(value = "需要升级模版")
        private Boolean needUpgrade;

        private Integer id;
        private Integer applicationId;
        @NotBlank(message = "任务名不能为空")
        private String name;
        @NotBlank(message = "任务key不能为空")
        private String jobKey;

        private Integer ciJobId;
        private Integer envType;
        private String jobType;
        private Integer jobBuildNumber;
        private Integer jobTplId;
        private Date updateTime;
        private Date createTime;
        private String parameterYaml;
        private String comment;

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
        private Integer cdJobId;
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
