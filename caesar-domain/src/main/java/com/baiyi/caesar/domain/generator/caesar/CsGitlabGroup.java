package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_gitlab_group")
public class CsGitlabGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    /**
     * 组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 可见
     */
    @Column(name = "group_visibility")
    private String groupVisibility;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_path")
    private String fullPath;

    @Column(name = "web_url")
    private String webUrl;

    /**
     * 绑定应用
     */
    @Column(name = "application_key")
    private String applicationKey;

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

    private String description;

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
     * 获取组id
     *
     * @return group_id - 组id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置组id
     *
     * @param groupId 组id
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取项目名称
     *
     * @return name - 项目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置项目名称
     *
     * @param name 项目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取路径
     *
     * @return path - 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置路径
     *
     * @param path 路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取可见
     *
     * @return group_visibility - 可见
     */
    public String getGroupVisibility() {
        return groupVisibility;
    }

    /**
     * 设置可见
     *
     * @param groupVisibility 可见
     */
    public void setGroupVisibility(String groupVisibility) {
        this.groupVisibility = groupVisibility;
    }

    /**
     * @return full_name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return full_path
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * @param fullPath
     */
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    /**
     * @return web_url
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * @param webUrl
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * 获取绑定应用
     *
     * @return application_key - 绑定应用
     */
    public String getApplicationKey() {
        return applicationKey;
    }

    /**
     * 设置绑定应用
     *
     * @param applicationKey 绑定应用
     */
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}