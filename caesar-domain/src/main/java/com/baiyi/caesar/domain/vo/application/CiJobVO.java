package com.baiyi.caesar.domain.vo.application;

import com.baiyi.caesar.domain.vo.aliyun.OssBucketVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static class CiJob {
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

        private Integer id;
        private Integer applicationId;
        private String name;
        private String jobKey;
        private String branch;
        private Integer envType;
        private String jobType;
        private Boolean enableTag;
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

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class JobEngine {

        @ApiModelProperty(value = "jenkins实例详情")
        private JenkinsInstanceVO.Instance jenkinsInstance;

        @ApiModelProperty(value = "最后构建任务url")
        private String lastBuildUrl;

        private Integer id;
        private Integer ciJobId;
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
