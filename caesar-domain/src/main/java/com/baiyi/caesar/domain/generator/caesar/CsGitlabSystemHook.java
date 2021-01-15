package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_gitlab_system_hook")
public class CsGitlabSystemHook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 群组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 名称
     */
    private String name;

    /**
     * 事件类型
     */
    @Column(name = "event_name")
    private String eventName;

    /**
     * 已消费数据
     */
    @Column(name = "is_consumed")
    private Boolean isConsumed;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "hooks_content")
    private String hooksContent;

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
     * 获取实例id
     *
     * @return instance_id - 实例id
     */
    public Integer getInstanceId() {
        return instanceId;
    }

    /**
     * 设置实例id
     *
     * @param instanceId 实例id
     */
    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * 获取项目id
     *
     * @return project_id - 项目id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目id
     *
     * @param projectId 项目id
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取群组id
     *
     * @return group_id - 群组id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置群组id
     *
     * @param groupId 群组id
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取事件类型
     *
     * @return event_name - 事件类型
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * 设置事件类型
     *
     * @param eventName 事件类型
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * 获取已消费数据
     *
     * @return is_consumed - 已消费数据
     */
    public Boolean getIsConsumed() {
        return isConsumed;
    }

    /**
     * 设置已消费数据
     *
     * @param isConsumed 已消费数据
     */
    public void setIsConsumed(Boolean isConsumed) {
        this.isConsumed = isConsumed;
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
     * @return hooks_content
     */
    public String getHooksContent() {
        return hooksContent;
    }

    /**
     * @param hooksContent
     */
    public void setHooksContent(String hooksContent) {
        this.hooksContent = hooksContent;
    }
}