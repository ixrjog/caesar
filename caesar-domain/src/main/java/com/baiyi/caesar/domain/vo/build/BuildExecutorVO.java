package com.baiyi.caesar.domain.vo.build;

import com.baiyi.caesar.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/8/31 2:54 下午
 * @Version 1.0
 */
public class BuildExecutorVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildExecutor {
        private ServerVO.Server server;

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
}
