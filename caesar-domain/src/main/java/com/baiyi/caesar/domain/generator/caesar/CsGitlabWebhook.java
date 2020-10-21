package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_gitlab_webhook")
public class CsGitlabWebhook {
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

    @Column(name = "before_commit")
    private String beforeCommit;

    @Column(name = "after_commit")
    private String afterCommit;

    private String ref;

    @Column(name = "user_id")
    private Integer userId;

    private String username;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "ssh_url")
    private String sshUrl;

    @Column(name = "web_url")
    private String webUrl;

    @Column(name = "http_url")
    private String httpUrl;

    private String homepage;

    @Column(name = "total_commits_count")
    private Integer totalCommitsCount;

    /**
     * 触发构建
     */
    @Column(name = "is_trigger")
    private Boolean isTrigger;

    /**
     * 任务key
     */
    @Column(name = "job_key")
    private String jobKey;

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
     * @return before_commit
     */
    public String getBeforeCommit() {
        return beforeCommit;
    }

    /**
     * @param beforeCommit
     */
    public void setBeforeCommit(String beforeCommit) {
        this.beforeCommit = beforeCommit;
    }

    /**
     * @return after_commit
     */
    public String getAfterCommit() {
        return afterCommit;
    }

    /**
     * @param afterCommit
     */
    public void setAfterCommit(String afterCommit) {
        this.afterCommit = afterCommit;
    }

    /**
     * @return ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return user_email
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
     * @return homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return total_commits_count
     */
    public Integer getTotalCommitsCount() {
        return totalCommitsCount;
    }

    /**
     * @param totalCommitsCount
     */
    public void setTotalCommitsCount(Integer totalCommitsCount) {
        this.totalCommitsCount = totalCommitsCount;
    }

    /**
     * 获取触发构建
     *
     * @return is_trigger - 触发构建
     */
    public Boolean getIsTrigger() {
        return isTrigger;
    }

    /**
     * 设置触发构建
     *
     * @param isTrigger 触发构建
     */
    public void setIsTrigger(Boolean isTrigger) {
        this.isTrigger = isTrigger;
    }

    /**
     * 获取任务key
     *
     * @return job_key - 任务key
     */
    public String getJobKey() {
        return jobKey;
    }

    /**
     * 设置任务key
     *
     * @param jobKey 任务key
     */
    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
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