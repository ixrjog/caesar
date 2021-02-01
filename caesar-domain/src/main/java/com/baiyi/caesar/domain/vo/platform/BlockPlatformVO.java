package com.baiyi.caesar.domain.vo.platform;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/28 11:42 上午
 * @Version 1.0
 */
public class BlockPlatformVO {


    @Data
    @Builder
    @ApiModel
    public static class BlockPlatformStatus {
        @Builder.Default
        private boolean isShow = false;
        @Builder.Default
        private List<BlockPlatform> blockPlatforms = Collections.EMPTY_LIST;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BlockPlatform {

        private Integer id;

        @ApiModelProperty(value = "封网级别 P0-P4")
        private String blockLevel;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "有效")
        private Boolean valid;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @ApiModelProperty(value = "开始时间")
        private Date startTime;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @ApiModelProperty(value = "结束时间")
        private Date endTime;

        private String comment;
    }

}
