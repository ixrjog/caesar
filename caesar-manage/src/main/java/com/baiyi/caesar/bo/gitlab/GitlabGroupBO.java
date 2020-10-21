package com.baiyi.caesar.bo.gitlab;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:27 下午
 * @Version 1.0
 */
@Data
@Builder
public class GitlabGroupBO {

    private Integer id;
    private Integer instanceId;
    private Integer groupId;
    private String name;
    private String path;
    private String groupVisibility;
    private String fullName;
    private String fullPath;
    private String webUrl;
    private String description;

}
