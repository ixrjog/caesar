package com.baiyi.caesar.domain.vo.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:17 下午
 * @Version 1.0
 */
public class TagVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Tag implements Serializable {

        private static final long serialVersionUID = 1445359231777384339L;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "标签key")
        private String tagKey;

        @ApiModelProperty(value = "颜色值")
        private String color;

        @ApiModelProperty(value = "描述")
        private String comment;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }

    public interface ITags {

        void setTags(List<Tag> tags);

        int getBusinessType();

        int getBusinessId();

    }

}
