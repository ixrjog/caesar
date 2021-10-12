package com.baiyi.caesar.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 5:09 下午
 * @Since 1.0
 */

@Data
@Builder
public class AliyunCRInstanceBO {

    private Integer id;

    private String regionId;

    private String instanceName;

    private String instanceId;

    @Builder.Default
    private Boolean isActive = false;

    private Date createTime;

    private Date updateTime;
}
