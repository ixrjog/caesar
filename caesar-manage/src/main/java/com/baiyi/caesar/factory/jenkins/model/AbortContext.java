package com.baiyi.caesar.factory.jenkins.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/9/15 3:33 下午
 * @Version 1.0
 */
@Data
@Builder
public class AbortContext {

    @Builder.Default
    private boolean isAbort = false;
    private String username;

}
