package com.baiyi.caesar.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/9/7 1:50 下午
 * @Version 1.0
 */
@Data
@Builder
public class CaesarInstanceBO {

    private Integer id;
    private String name;
    private String hostName;
    private String hostIp;
    @Builder.Default
    private Integer instanceStatus = 0;
    @Builder.Default
    private Boolean isActive = true;
    private Date createTime;
    private Date updateTime;
    private String comment;
}
