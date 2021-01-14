package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_ci_job_build")
public class CsCiJobBuild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ci_job_id")
    private Integer ciJobId;

    @Column(name = "job_engine_id")
    private Integer jobEngineId;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 默认分支
     */
    private String branch;

    /**
     * 用户名
     */
    private String username;

    /**
     * 当前构建编号
     */
    @Column(name = "job_build_number")
    private Integer jobBuildNumber;

    /**
     * 引擎构建编号
     */
    @Column(name = "engine_build_number")
    private Integer engineBuildNumber;

    @Column(name = "version_name")
    private String versionName;

    @Column(name = "version_desc")
    private String versionDesc;

    private String commit;

    @Column(name = "build_phase")
    private String buildPhase;

    @Column(name = "build_status")
    private String buildStatus;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 是否结束
     */
    private Boolean finalized;

    /**
     * 操作用户
     */
    @Column(name = "operation_username")
    private String operationUsername;

    /**
     * 静默
     */
    @Column(name = "is_silence")
    private Boolean isSilence;

    /**
     * 是否回滚操作
     */
    @Column(name = "is_rollback")
    private Boolean isRollback;

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
     * 任务参数配置
     */
    private String parameters;

    /**
     * 钉钉消息
     */
    @Column(name = "dingtalk_msg")
    private String dingtalkMsg;

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
     * @return job_engine_id
     */
    public Integer getJobEngineId() {
        return jobEngineId;
    }

    /**
     * @param jobEngineId
     */
    public void setJobEngineId(Integer jobEngineId) {
        this.jobEngineId = jobEngineId;
    }

    /**
     * 获取任务名称
     *
     * @return job_name - 任务名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置任务名称
     *
     * @param jobName 任务名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
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
     * 获取引擎构建编号
     *
     * @return engine_build_number - 引擎构建编号
     */
    public Integer getEngineBuildNumber() {
        return engineBuildNumber;
    }

    /**
     * 设置引擎构建编号
     *
     * @param engineBuildNumber 引擎构建编号
     */
    public void setEngineBuildNumber(Integer engineBuildNumber) {
        this.engineBuildNumber = engineBuildNumber;
    }

    /**
     * @return version_name
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return version_desc
     */
    public String getVersionDesc() {
        return versionDesc;
    }

    /**
     * @param versionDesc
     */
    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    /**
     * @return commit
     */
    public String getCommit() {
        return commit;
    }

    /**
     * @param commit
     */
    public void setCommit(String commit) {
        this.commit = commit;
    }

    /**
     * @return build_phase
     */
    public String getBuildPhase() {
        return buildPhase;
    }

    /**
     * @param buildPhase
     */
    public void setBuildPhase(String buildPhase) {
        this.buildPhase = buildPhase;
    }

    /**
     * @return build_status
     */
    public String getBuildStatus() {
        return buildStatus;
    }

    /**
     * @param buildStatus
     */
    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取是否结束
     *
     * @return finalized - 是否结束
     */
    public Boolean getFinalized() {
        return finalized;
    }

    /**
     * 设置是否结束
     *
     * @param finalized 是否结束
     */
    public void setFinalized(Boolean finalized) {
        this.finalized = finalized;
    }

    /**
     * 获取操作用户
     *
     * @return operation_username - 操作用户
     */
    public String getOperationUsername() {
        return operationUsername;
    }

    /**
     * 设置操作用户
     *
     * @param operationUsername 操作用户
     */
    public void setOperationUsername(String operationUsername) {
        this.operationUsername = operationUsername;
    }

    /**
     * 获取静默
     *
     * @return is_silence - 静默
     */
    public Boolean getIsSilence() {
        return isSilence;
    }

    /**
     * 设置静默
     *
     * @param isSilence 静默
     */
    public void setIsSilence(Boolean isSilence) {
        this.isSilence = isSilence;
    }

    /**
     * 获取是否回滚操作
     *
     * @return is_rollback - 是否回滚操作
     */
    public Boolean getIsRollback() {
        return isRollback;
    }

    /**
     * 设置是否回滚操作
     *
     * @param isRollback 是否回滚操作
     */
    public void setIsRollback(Boolean isRollback) {
        this.isRollback = isRollback;
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
     * 获取任务参数配置
     *
     * @return parameters - 任务参数配置
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * 设置任务参数配置
     *
     * @param parameters 任务参数配置
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * 获取钉钉消息
     *
     * @return dingtalk_msg - 钉钉消息
     */
    public String getDingtalkMsg() {
        return dingtalkMsg;
    }

    /**
     * 设置钉钉消息
     *
     * @param dingtalkMsg 钉钉消息
     */
    public void setDingtalkMsg(String dingtalkMsg) {
        this.dingtalkMsg = dingtalkMsg;
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