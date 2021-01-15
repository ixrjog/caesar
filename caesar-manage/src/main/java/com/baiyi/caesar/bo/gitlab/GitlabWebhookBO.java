package com.baiyi.caesar.bo.gitlab;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/10/21 10:57 上午
 * @Version 1.0
 */
@Data
@Builder
public class GitlabWebhookBO {

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
    @Builder.Default
    private Boolean isTrigger = false;
    private String jobKey;
    @Builder.Default
    private Boolean isConsumed = false;
    private String hooksContent;

}
