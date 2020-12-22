package com.baiyi.caesar.bo.gitlab;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/12/22 2:00 下午
 * @Version 1.0
 */
@Data
@Builder
public class GitlabSystemHookBO {

    private Integer id;
    private Integer instanceId;
    private Integer projectId;
    private Integer groupId;
    private String name;
    private String eventName;
    @Builder.Default
    private Boolean isConsumed = false;
    private String hooksContent;
}
