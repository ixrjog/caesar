package com.baiyi.caesar.domain.vo.application;

import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

        private EnvVO.Env env;
        private DingtalkVO.Dingtalk dingtalk;
        private List<TagVO.Tag> tags;

        private Integer id;
        private Integer applicationId;
        private String name;
        private String jobKey;
        private String branch;
        private Integer envType;
        private String jobType;
        private Boolean enableTag;
        private Integer scmMemberId;
        private Integer dingtalkId;
        private Integer jobBuildNumber;
        private Boolean hide;
        private Integer deploymentJobId;
        private Boolean atAll;
        private Date updateTime;
        private Date createTime;
        private String parameterYaml;
        private String comment;

    }
}
