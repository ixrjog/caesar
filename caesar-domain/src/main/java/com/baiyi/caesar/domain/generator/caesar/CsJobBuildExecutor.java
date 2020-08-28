package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_job_build_executor")
public class CsJobBuildExecutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "build_type")
    private Integer buildType;

    @Column(name = "build_id")
    private Integer buildId;

    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    @Column(name = "node_name")
    private String nodeName;

    /**
     * 管理ip
     */
    @Column(name = "private_ip")
    private String privateIp;

    /**
     * Remote root directory
     */
    @Column(name = "root_directory")
    private String rootDirectory;

    /**
     * 工作目录
     */
    private String workspace;

    /**
     * 任务url
     */
    @Column(name = "job_url")
    private String jobUrl;

    /**
     * 构建编号
     */
    @Column(name = "build_number")
    private Integer buildNumber;

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
     * @return build_id
     */
    public Integer getBuildId() {
        return buildId;
    }

    /**
     * @param buildId
     */
    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    /**
     * @return job_id
     */
    public Integer getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     */
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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
     * @return node_name
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * 获取管理ip
     *
     * @return private_ip - 管理ip
     */
    public String getPrivateIp() {
        return privateIp;
    }

    /**
     * 设置管理ip
     *
     * @param privateIp 管理ip
     */
    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    /**
     * 获取Remote root directory
     *
     * @return root_directory - Remote root directory
     */
    public String getRootDirectory() {
        return rootDirectory;
    }

    /**
     * 设置Remote root directory
     *
     * @param rootDirectory Remote root directory
     */
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    /**
     * 获取工作目录
     *
     * @return workspace - 工作目录
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * 设置工作目录
     *
     * @param workspace 工作目录
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * 获取任务url
     *
     * @return job_url - 任务url
     */
    public String getJobUrl() {
        return jobUrl;
    }

    /**
     * 设置任务url
     *
     * @param jobUrl 任务url
     */
    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    /**
     * 获取构建编号
     *
     * @return build_number - 构建编号
     */
    public Integer getBuildNumber() {
        return buildNumber;
    }

    /**
     * 设置构建编号
     *
     * @param buildNumber 构建编号
     */
    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
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
}