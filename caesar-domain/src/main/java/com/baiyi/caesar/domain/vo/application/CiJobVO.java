package com.baiyi.caesar.domain.vo.application;

import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.sonar.SonarQubeVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;
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
 * @Date 2020/7/29 2:01 下午
 * @Version 1.0
 */
public class CiJobVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CiJob implements EnvVO.IEnv, TagVO.ITags, JobTplVO.IJobTpl,
            OssBucketVO.IBucket, DingtalkVO.IDingtalk, CiJobBuildVO.IBuildView, SonarQubeVO.ISonarQube,
            CdJobVO.IDeploymentJob, JenkinsVO.IJenkinsParameter {

        private int businessType;

        @Override
        public int getBusinessId() {
            return scmMember.getScmId();
        }

        @Override
        public Integer getCiJobId() {
            return this.id;
        }

        @Override
        public Boolean enableSonar() {
            return this.enableSonar;
        }

        @ApiModelProperty(value = "环境详情")
        private EnvVO.Env env;

        @ApiModelProperty(value = "钉钉详情")
        private DingtalkVO.Dingtalk dingtalk;

        @ApiModelProperty(value = "标签")
        private List<TagVO.Tag> tags;

        @ApiModelProperty(value = "任务模版")
        private JobTplVO.JobTpl jobTpl;

        private ApplicationVO.ScmMember scmMember;

        @ApiModelProperty(value = "对象存储")
        private OssBucketVO.Bucket bucket;

        @ApiModelProperty(value = "构建视图")
        private List<CiJobBuildVO.JobBuildView> buildViews;

        @ApiModelProperty(value = "任务参数Map")
        private Map<String, String> parameters;

        @ApiModelProperty(value = "任务引擎")
        private List<JobEngineVO.JobEngine> jobEngines;

        @ApiModelProperty(value = "质量管理")
        private SonarQubeVO.SonarQube sonarQube;

        @ApiModelProperty(value = "需要升级模版")
        private Boolean needUpgrade;

        @ApiModelProperty(value = "部署任务")
        private CdJobVO.CdJob cdJob;

        private Integer id;
        private Integer applicationId;
        @NotBlank(message = "任务名不能为空")
        private String name;
        @NotBlank(message = "任务key不能为空")
        private String jobKey;
        private String branch;
        private Integer envType;
        private String jobType;
        private Boolean enableTag;
        private Boolean enableSonar;
        private Integer scmMemberId;
        private Integer ossBucketId;
        private Integer dingtalkId;
        private Integer jobBuildNumber;
        private Boolean hide;
        private Integer jobTplId;
        private Integer deploymentJobId;
        private Boolean atAll;
        private Date updateTime;
        private Date createTime;
        private String parameterYaml;
        private String comment;

    }


}
