package com.baiyi.caesar.domain.vo.caesar;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/9/8 1:54 下午
 * @Version 1.0
 */
public class CaesarVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance {

        private Integer id;

        private String name;

        private String hostName;

        private String hostIp;

        private Integer instanceStatus;

        private Boolean isActive;

        private Date createTime;

        private Date updateTime;

        private String comment;
    }
}
