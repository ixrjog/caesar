package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_job_engine")
public class CsJobEngine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "build_type")
    private Integer buildType;

    /**
     * 任务id
     */
    @Column(name = "job_id")
    private Integer jobId;

    /**
     * jenkins实例id
     */
    @Column(name = "jenkins_instance_id")
    private Integer jenkinsInstanceId;

    /**
     * (完整)任务名称
     */
    private String name;

    /**
     * jenkins job url
     */
    @Column(name = "job_url")
    private String jobUrl;

    /**
     * 最后构建任务编号
     */
    @Column(name = "last_build_number")
    private Integer lastBuildNumber;

    /**
     * 下次构建任务编号
     */
    @Column(name = "next_build_number")
    private Integer nextBuildNumber;

    /**
     * 最后构建完成任务编号
     */
    @Column(name = "last_completed_build_number")
    private Integer lastCompletedBuildNumber;

    /**
     * 最后构建成功任务编号
     */
    @Column(name = "last_successful_build_number")
    private Integer lastSuccessfulBuildNumber;

    /**
     * 最后构建失败任务编号
     */
    @Column(name = "last_failed_build_number")
    private Integer lastFailedBuildNumber;

    /**
     * 当前任务状态
     */
    @Column(name = "job_status")
    private Integer jobStatus;

    @Column(name = "tpl_version")
    private Integer tplVersion;

    @Column(name = "tpl_hash")
    private String tplHash;

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
     * @return build_type
     */
    public Integer getBuildType() {
        return buildType;
    }

    /**
     * @param buildType
     */
    public void setBuildType(Integer buildType) {
        this.buildType = buildType;
    }

    /**
     * 获取任务id
     *
     * @return job_id - 任务id
     */
    public Integer getJobId() {
        return jobId;
    }

    /**
     * 设置任务id
     *
     * @param jobId 任务id
     */
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取jenkins实例id
     *
     * @return jenkins_instance_id - jenkins实例id
     */
    public Integer getJenkinsInstanceId() {
        return jenkinsInstanceId;
    }

    /**
     * 设置jenkins实例id
     *
     * @param jenkinsInstanceId jenkins实例id
     */
    public void setJenkinsInstanceId(Integer jenkinsInstanceId) {
        this.jenkinsInstanceId = jenkinsInstanceId;
    }

    /**
     * 获取(完整)任务名称
     *
     * @return name - (完整)任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置(完整)任务名称
     *
     * @param name (完整)任务名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取jenkins job url
     *
     * @return job_url - jenkins job url
     */
    public String getJobUrl() {
        return jobUrl;
    }

    /**
     * 设置jenkins job url
     *
     * @param jobUrl jenkins job url
     */
    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    /**
     * 获取最后构建任务编号
     *
     * @return last_build_number - 最后构建任务编号
     */
    public Integer getLastBuildNumber() {
        return lastBuildNumber;
    }

    /**
     * 设置最后构建任务编号
     *
     * @param lastBuildNumber 最后构建任务编号
     */
    public void setLastBuildNumber(Integer lastBuildNumber) {
        this.lastBuildNumber = lastBuildNumber;
    }

    /**
     * 获取下次构建任务编号
     *
     * @return next_build_number - 下次构建任务编号
     */
    public Integer getNextBuildNumber() {
        return nextBuildNumber;
    }

    /**
     * 设置下次构建任务编号
     *
     * @param nextBuildNumber 下次构建任务编号
     */
    public void setNextBuildNumber(Integer nextBuildNumber) {
        this.nextBuildNumber = nextBuildNumber;
    }

    /**
     * 获取最后构建完成任务编号
     *
     * @return last_completed_build_number - 最后构建完成任务编号
     */
    public Integer getLastCompletedBuildNumber() {
        return lastCompletedBuildNumber;
    }

    /**
     * 设置最后构建完成任务编号
     *
     * @param lastCompletedBuildNumber 最后构建完成任务编号
     */
    public void setLastCompletedBuildNumber(Integer lastCompletedBuildNumber) {
        this.lastCompletedBuildNumber = lastCompletedBuildNumber;
    }

    /**
     * 获取最后构建成功任务编号
     *
     * @return last_successful_build_number - 最后构建成功任务编号
     */
    public Integer getLastSuccessfulBuildNumber() {
        return lastSuccessfulBuildNumber;
    }

    /**
     * 设置最后构建成功任务编号
     *
     * @param lastSuccessfulBuildNumber 最后构建成功任务编号
     */
    public void setLastSuccessfulBuildNumber(Integer lastSuccessfulBuildNumber) {
        this.lastSuccessfulBuildNumber = lastSuccessfulBuildNumber;
    }

    /**
     * 获取最后构建失败任务编号
     *
     * @return last_failed_build_number - 最后构建失败任务编号
     */
    public Integer getLastFailedBuildNumber() {
        return lastFailedBuildNumber;
    }

    /**
     * 设置最后构建失败任务编号
     *
     * @param lastFailedBuildNumber 最后构建失败任务编号
     */
    public void setLastFailedBuildNumber(Integer lastFailedBuildNumber) {
        this.lastFailedBuildNumber = lastFailedBuildNumber;
    }

    /**
     * 获取当前任务状态
     *
     * @return job_status - 当前任务状态
     */
    public Integer getJobStatus() {
        return jobStatus;
    }

    /**
     * 设置当前任务状态
     *
     * @param jobStatus 当前任务状态
     */
    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
     * @return tpl_version
     */
    public Integer getTplVersion() {
        return tplVersion;
    }

    /**
     * @param tplVersion
     */
    public void setTplVersion(Integer tplVersion) {
        this.tplVersion = tplVersion;
    }

    /**
     * @return tpl_hash
     */
    public String getTplHash() {
        return tplHash;
    }

    /**
     * @param tplHash
     */
    public void setTplHash(String tplHash) {
        this.tplHash = tplHash;
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