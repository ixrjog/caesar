package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_ci_job")
public class CsCiJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务key
     */
    @Column(name = "job_key")
    private String jobKey;

    /**
     * 默认分支
     */
    private String branch;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 任务类型
     */
    @Column(name = "job_type")
    private String jobType;

    /**
     * 启用Tag
     */
    @Column(name = "enable_tag")
    private Boolean enableTag;

    /**
     * 仓库成员id
     */
    @Column(name = "scm_member_id")
    private Integer scmMemberId;

    /**
     * 当前构建编号
     */
    @Column(name = "job_build_number")
    private Integer jobBuildNumber;

    /**
     * 隐藏任务
     */
    private Boolean hide;

    /**
     * 部署任务id
     */
    @Column(name = "deployment_job_id")
    private Integer deploymentJobId;

    /**
     * 通知所有人
     */
    @Column(name = "at_all")
    private Boolean atAll;

    /**
     * 钉钉id
     */
    @Column(name = "dingtalk_id")
    private Integer dingtalkId;

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

    /**
     * 任务参数配置
     */
    @Column(name = "parameter_yaml")
    private String parameterYaml;

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
     * @return application_id
     */
    public Integer getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId
     */
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 获取任务名称
     *
     * @return name - 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置任务名称
     *
     * @param name 任务名称
     */
    public void setName(String name) {
        this.name = name;
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
     * 获取默认分支
     *
     * @return branch - 默认分支
     */
    public String getBranch() {
        return branch;
    }

    /**
     * 设置默认分支
     *
     * @param branch 默认分支
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * 获取环境类型
     *
     * @return env_type - 环境类型
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * 设置环境类型
     *
     * @param envType 环境类型
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
    }

    /**
     * 获取任务类型
     *
     * @return job_type - 任务类型
     */
    public String getJobType() {
        return jobType;
    }

    /**
     * 设置任务类型
     *
     * @param jobType 任务类型
     */
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    /**
     * 获取启用Tag
     *
     * @return enable_tag - 启用Tag
     */
    public Boolean getEnableTag() {
        return enableTag;
    }

    /**
     * 设置启用Tag
     *
     * @param enableTag 启用Tag
     */
    public void setEnableTag(Boolean enableTag) {
        this.enableTag = enableTag;
    }

    /**
     * 获取仓库成员id
     *
     * @return scm_member_id - 仓库成员id
     */
    public Integer getScmMemberId() {
        return scmMemberId;
    }

    /**
     * 设置仓库成员id
     *
     * @param scmMemberId 仓库成员id
     */
    public void setScmMemberId(Integer scmMemberId) {
        this.scmMemberId = scmMemberId;
    }

    /**
     * 获取当前构建编号
     *
     * @return job_build_number - 当前构建编号
     */
    public Integer getJobBuildNumber() {
        return jobBuildNumber;
    }

    /**
     * 设置当前构建编号
     *
     * @param jobBuildNumber 当前构建编号
     */
    public void setJobBuildNumber(Integer jobBuildNumber) {
        this.jobBuildNumber = jobBuildNumber;
    }

    /**
     * 获取隐藏任务
     *
     * @return hide - 隐藏任务
     */
    public Boolean getHide() {
        return hide;
    }

    /**
     * 设置隐藏任务
     *
     * @param hide 隐藏任务
     */
    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    /**
     * 获取部署任务id
     *
     * @return deployment_job_id - 部署任务id
     */
    public Integer getDeploymentJobId() {
        return deploymentJobId;
    }

    /**
     * 设置部署任务id
     *
     * @param deploymentJobId 部署任务id
     */
    public void setDeploymentJobId(Integer deploymentJobId) {
        this.deploymentJobId = deploymentJobId;
    }

    /**
     * 获取通知所有人
     *
     * @return at_all - 通知所有人
     */
    public Boolean getAtAll() {
        return atAll;
    }

    /**
     * 设置通知所有人
     *
     * @param atAll 通知所有人
     */
    public void setAtAll(Boolean atAll) {
        this.atAll = atAll;
    }

    /**
     * 获取钉钉id
     *
     * @return dingtalk_id - 钉钉id
     */
    public Integer getDingtalkId() {
        return dingtalkId;
    }

    /**
     * 设置钉钉id
     *
     * @param dingtalkId 钉钉id
     */
    public void setDingtalkId(Integer dingtalkId) {
        this.dingtalkId = dingtalkId;
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
     * 获取任务参数配置
     *
     * @return parameter_yaml - 任务参数配置
     */
    public String getParameterYaml() {
        return parameterYaml;
    }

    /**
     * 设置任务参数配置
     *
     * @param parameterYaml 任务参数配置
     */
    public void setParameterYaml(String parameterYaml) {
        this.parameterYaml = parameterYaml;
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