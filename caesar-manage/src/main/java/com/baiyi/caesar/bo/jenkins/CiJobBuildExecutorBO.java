package com.baiyi.caesar.bo.jenkins;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/12 4:34 下午
 * @Version 1.0
 */
@Data
@Builder
public class CiJobBuildExecutorBO {

    private Integer id;
    private Integer buildId;
    private Integer ciJobId;
    private String jobName;
    private String nodeName;
    private String privateIp;
    private String rootDirectory;
    private String workspace;
    private String jobUrl;
    private Integer buildNumber;
    private Date createTime;
    private Date updateTime;

}
