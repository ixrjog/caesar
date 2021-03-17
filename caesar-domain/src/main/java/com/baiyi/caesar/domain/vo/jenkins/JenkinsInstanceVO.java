package com.baiyi.caesar.domain.vo.jenkins;

import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/17 5:51 下午
 * @Version 1.0
 */
public class JenkinsInstanceVO {

    public interface IInstance {
        void setInstance(JenkinsInstanceVO.Instance instance);

        Integer getInstanceId();
    }

    public interface IJenkinsVersion {

        void setVersion(String version);

        /**
         * 实例名称
         *
         * @return
         */
        String getInstanceName();
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance implements ServerVO.IServer, IJenkinsVersion, ServerGroupVO.IServerGroup {

        @Override
        public Integer getServerGroupId() {
            return nodeServerGroupId;
        }

        @Override
        public String getInstanceName() {
            return this.name;
        }

        @Override
        public void setServerGroup(ServerGroupVO.ServerGroup serverGroup) {
            this.nodeGroup = serverGroup;
        }

        @ApiModelProperty(value = "jenkins服务器")
        private ServerVO.Server server;

        @ApiModelProperty(value = "节点服务器组")
        private ServerGroupVO.ServerGroup nodeGroup;

        @ApiModelProperty(value = "节点服务器")
        private List<ServerVO.Server> nodes;

        @ApiModelProperty(value = "版本详情")
        private String version;

        private Integer id;

        @ApiModelProperty(value = "实例名称")
        @NotNull
        private String name;

        @ApiModelProperty(value = "管理url")
        @NotNull
        private String url;

        @ApiModelProperty(value = "用户名")
        @NotNull
        private String username;

        @ApiModelProperty(value = "passwordOrToken")
        private String token;

        @ApiModelProperty(value = "是否有效")
        @NotNull
        private Boolean isActive;

        @ApiModelProperty(value = "绑定服务器id")
        @NotNull
        private Integer serverId;

        @ApiModelProperty(value = "绑定节点服务器组id")
        @NotNull
        private Integer nodeServerGroupId;

        @ApiModelProperty(value = "实例状态")
        private Integer instanceStatus;

        private Date createTime;

        private Date updateTime;

        private String comment;
    }

}
