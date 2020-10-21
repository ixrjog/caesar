package com.baiyi.caesar.domain.param.gitlab;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:40 下午
 * @Version 1.0
 */
public class GitlabGroupParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GitlabGroupPageQuery extends GitlabProjectParam.GitlabProjectPageQuery {
    }
}
