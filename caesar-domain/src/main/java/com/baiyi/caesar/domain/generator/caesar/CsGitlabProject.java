package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_gitlab_project")
public class CsGitlabProject {
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
    @Column(name = "project_visibility")
    private String projectVisibility;

    @Column(name = "namespace_id")
    private Integer namespaceId;

    @Column(name = "namespace_name")
    private String namespaceName;

    /**
     * 命名空间路径
     */
    @Column(name = "namespace_path")
    private String namespacePath;

    @Column(name = "namespace_kind")
    private String namespaceKind;

    @Column(name = "namespace_full_path")
    private String namespaceFullPath;

    @Column(name = "ssh_url")
    private String sshUrl;

    @Column(name = "web_url")
    private String webUrl;

    @Column(name = "http_url")
    private String httpUrl;

    /**
     * 默认分支
     */
    @Column(name = "default_branch")
    private String defaultBranch;

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
     * @return project_visibility - 可见
     */
    public String getProjectVisibility() {
        return projectVisibility;
    }

    /**
     * 设置可见
     *
     * @param projectVisibility 可见
     */
    public void setProjectVisibility(String projectVisibility) {
        this.projectVisibility = projectVisibility;
    }

    /**
     * @return namespace_id
     */
    public Integer getNamespaceId() {
        return namespaceId;
    }

    /**
     * @param namespaceId
     */
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    /**
     * @return namespace_name
     */
    public String getNamespaceName() {
        return namespaceName;
    }

    /**
     * @param namespaceName
     */
    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    /**
     * 获取命名空间路径
     *
     * @return namespace_path - 命名空间路径
     */
    public String getNamespacePath() {
        return namespacePath;
    }

    /**
     * 设置命名空间路径
     *
     * @param namespacePath 命名空间路径
     */
    public void setNamespacePath(String namespacePath) {
        this.namespacePath = namespacePath;
    }

    /**
     * @return namespace_kind
     */
    public String getNamespaceKind() {
        return namespaceKind;
    }

    /**
     * @param namespaceKind
     */
    public void setNamespaceKind(String namespaceKind) {
        this.namespaceKind = namespaceKind;
    }

    /**
     * @return namespace_full_path
     */
    public String getNamespaceFullPath() {
        return namespaceFullPath;
    }

    /**
     * @param namespaceFullPath
     */
    public void setNamespaceFullPath(String namespaceFullPath) {
        this.namespaceFullPath = namespaceFullPath;
    }

    /**
     * @return ssh_url
     */
    public String getSshUrl() {
        return sshUrl;
    }

    /**
     * @param sshUrl
     */
    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
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
     * @return http_url
     */
    public String getHttpUrl() {
        return httpUrl;
    }

    /**
     * @param httpUrl
     */
    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    /**
     * 获取默认分支
     *
     * @return default_branch - 默认分支
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * 设置默认分支
     *
     * @param defaultBranch 默认分支
     */
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
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