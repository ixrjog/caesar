package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_instance")
public class CsInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例名
     */
    private String name;

    /**
     * 主机名
     */
    @Column(name = "host_name")
    private String hostName;

    /**
     * 主机ip
     */
    @Column(name = "host_ip")
    private String hostIp;

    /**
     * 实例状态
     */
    @Column(name = "instance_status")
    private Integer instanceStatus;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 描述
     */
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
     * 获取实例名
     *
     * @return name - 实例名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置实例名
     *
     * @param name 实例名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取主机名
     *
     * @return host_name - 主机名
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * 设置主机名
     *
     * @param hostName 主机名
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * 获取主机ip
     *
     * @return host_ip - 主机ip
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * 设置主机ip
     *
     * @param hostIp 主机ip
     */
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
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
     * 获取描述
     *
     * @return comment - 描述
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置描述
     *
     * @param comment 描述
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}