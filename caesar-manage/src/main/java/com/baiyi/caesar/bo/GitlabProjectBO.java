package com.baiyi.caesar.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/20 11:44 上午
 * @Version 1.0
 */
@Data
@Builder
public class GitlabProjectBO {

    private Integer id;
    private Integer instanceId;
    private Integer projectId;
    private String name;
    private String path;
    private String projectVisibility;
    private Integer namespaceId;
    private String namespaceName;
    private String namespacePath;
    private String namespaceKind;
    private String namespaceFullPath;
    private String sshUrl;
    private String webUrl;
    private String httpUrl;
    private String defaultBranch;
    private Date createTime;
    private Date updateTime;
    private String description;
}
