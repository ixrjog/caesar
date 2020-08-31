package com.baiyi.caesar.domain.generator.caesar;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cs_jenkins_instance")
public class CsJenkinsInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例名称
     */
    private String name;

    /**
     * 管理url
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * passwordOrToken
     */
    private String token;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 绑定服务器id
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 绑定节点服务器组
     */
    @Column(name = "node_server_group_id")
    private Integer nodeServerGroupId;

    /**
     * 实例状态
     */
    @Column(name = "instance_status")
    private Integer instanceStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取实例名称
     *
     * @return name - 实例名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置实例名称
     *
     * @param name 实例名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取管理url
     *
     * @return url - 管理url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置管理url
     *
     * @param url 管理url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取passwordOrToken
     *
     * @return token - passwordOrToken
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置passwordOrToken
     *
     * @param token passwordOrToken
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取有效
     *
     * @return is_active - 有效
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * 设置有效
     *
     * @param isActive 有效
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * 获取绑定服务器id
     *
     * @return server_id - 绑定服务器id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * 设置绑定服务器id
     *
     * @param serverId 绑定服务器id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取绑定节点服务器组
     *
     * @return node_server_group_id - 绑定节点服务器组
     */
    public Integer getNodeServerGroupId() {
        return nodeServerGroupId;
    }

    /**
     * 设置绑定节点服务器组
     *
     * @param nodeServerGroupId 绑定节点服务器组
     */
    public void setNodeServerGroupId(Integer nodeServerGroupId) {
        this.nodeServerGroupId = nodeServerGroupId;
    }

    /**
     * 获取实例状态
     *
     * @return instance_status - 实例状态
     */
    public Integer getInstanceStatus() {
        return instanceStatus;
    }

    /**
     * 设置实例状态
     *
     * @param instanceStatus 实例状态
     */
    public void setInstanceStatus(Integer instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}