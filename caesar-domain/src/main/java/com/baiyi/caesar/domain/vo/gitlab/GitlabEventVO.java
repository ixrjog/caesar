package com.baiyi.caesar.domain.vo.gitlab;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/5/10 1:40 下午
 * @Version 1.0
 */
public class GitlabEventVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Event  implements GitlabInstanceVO.IInstance {

        private GitlabInstanceVO.Instance instance;

        private Integer id;

        private Integer instanceId;

        private Integer projectId;

        private String objectKind;

        private String name;

        private String beforeCommit;

        private String afterCommit;

        private String ref;

        private Integer userId;

        private String username;

        private String userEmail;

        private String sshUrl;

        private String webUrl;

        private String httpUrl;

        private String homepage;

        private Integer totalCommitsCount;

        private Boolean isTrigger;

        private String jobKey;

        private Boolean isConsumed;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        private String hooksContent;


    }
}
