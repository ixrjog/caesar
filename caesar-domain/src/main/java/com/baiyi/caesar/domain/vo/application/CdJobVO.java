package com.baiyi.caesar.domain.vo.application;

import com.baiyi.caesar.domain.vo.build.BuildViewVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsVO;
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

    public interface IDeploymentJob {

        Integer getDeploymentJobId();

        void setCdJob(CdJobVO.CdJob cdJob);
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CdJob implements EnvVO.IEnv, JobTplVO.IJobTpl, CdJobBuildVO.IBuildView, JenkinsVO.IJenkinsParameter {

        @Override
        public Integer getCdJobId() {
            return this.id;
        }

        @ApiModelProperty(value = "环境详情")
        private EnvVO.Env env;

        @ApiModelProperty(value = "任务模版")
        private JobTplVO.JobTpl jobTpl;

        @ApiModelProperty(value = "构建视图")
        private List<BuildViewVO.JobBuildView> buildViews;

        @ApiModelProperty(value = "任务参数Map")
        private Map<String, String> parameters;

        @ApiModelProperty(value = "任务引擎")
        private List<JobEngineVO.JobEngine> jobEngines;

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

}
