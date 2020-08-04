package com.baiyi.caesar.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:37 下午
 * @Version 1.0
 */
@Builder
@Data
public class OssBucketBO {

    private Integer id;

    private String name;

    private String bucketLocation;

    private String extranetEndpoint;

    private String intranetEndpoint;

    private String bucketRegion;

    private Date createTime;

    private Date updateTime;

    @Builder.Default
    private Boolean isActive = false;

    private String comment;
}
