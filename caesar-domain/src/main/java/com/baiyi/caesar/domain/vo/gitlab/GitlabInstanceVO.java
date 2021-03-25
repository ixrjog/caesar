package com.baiyi.caesar.domain.vo.gitlab;

import com.baiyi.caesar.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:44 上午
 * @Version 1.0
 */
public class GitlabInstanceVO {

    public interface IInstance {

        void setInstance(GitlabInstanceVO.Instance instance);

        Integer getInstanceId();
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance implements ServerVO.IServer,IVersion  {

        @Override
        public String getInstanceName(){
            return this.name;
        }

        @ApiModelProperty(value = "jenkins服务器")
        private ServerVO.Server server;

        @ApiModelProperty(value = "版本详情")
        private Version version;

        @ApiModelProperty(value = "项目数量")
        private Integer projectSize;

        private Integer id;

        private String name;

        private String url;

        private String token;

        private Boolean isActive;

        private Integer serverId;

        private Date createTime;

        private Date updateTime;

        private String comment;

    }

    public interface IVersion {

        void setVersion(Version version);

        /**
         * 实例名称
         * @return
         */
        String getInstanceName();

    }

    @Data
    @Builder
    @ApiModel
    public static class Version {
        private String version;
        private String revision;
    }
}
