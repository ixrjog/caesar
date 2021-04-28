package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_cd_job")
public class CsCdJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "ci_job_id")
    private Integer ciJobId;

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
     * 当前构建编号
     */
    @Column(name = "job_build_number")
    private Integer jobBuildNumber;

    /**
     * 模版id
     */
    @Column(name = "job_tpl_id")
    private Integer jobTplId;

    private String href;

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
     * @return ci_job_id
     */
    public Integer getCiJobId() {
        return ciJobId;
    }

    /**
     * @param ciJobId
     */
    public void setCiJobId(Integer ciJobId) {
        this.ciJobId = ciJobId;
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
     * 获取模版id
     *
     * @return job_tpl_id - 模版id
     */
    public Integer getJobTplId() {
        return jobTplId;
    }

    /**
     * 设置模版id
     *
     * @param jobTplId 模版id
     */
    public void setJobTplId(Integer jobTplId) {
        this.jobTplId = jobTplId;
    }

    /**
     * @return href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     */
    public void setHref(String href) {
        this.href = href;
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