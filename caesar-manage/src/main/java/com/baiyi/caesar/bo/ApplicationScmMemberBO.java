package com.baiyi.caesar.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/22 4:11 下午
 * @Version 1.0
 */
@Builder
@Data
public class ApplicationScmMemberBO {

    private Integer id;
    private Integer applicationId;
    private String scmType;
    private Integer scmId;
    private String scmSshUrl;
    private Date createTime;
    private Date updateTime;
    private String comment;
}
