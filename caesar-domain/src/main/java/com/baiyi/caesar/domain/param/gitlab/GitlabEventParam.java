package com.baiyi.caesar.domain.param.gitlab;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/10 1:41 下午
 * @Version 1.0
 */
public class GitlabEventParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class GitlabEventPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "实例id", example = "1")
        private Integer instanceId;

        @ApiModelProperty(value = "事件分类",example = "push")
        private String kind;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }
}
