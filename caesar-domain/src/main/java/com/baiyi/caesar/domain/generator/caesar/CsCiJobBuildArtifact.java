package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_ci_job_build_artifact")
public class CsCiJobBuildArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "build_id")
    private Integer buildId;

    @Column(name = "ci_job_id")
    private Integer ciJobId;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    @Column(name = "artifact_display_path")
    private String artifactDisplayPath;

    @Column(name = "artifact_file_name")
    private String artifactFileName;

    @Column(name = "artifact_relative_path")
    private String artifactRelativePath;

    @Column(name = "artifact_size")
    private Long artifactSize;

    @Column(name = "storage_path")
    private String storagePath;

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
     * @return artifact_display_path
     */
    public String getArtifactDisplayPath() {
        return artifactDisplayPath;
    }

    /**
     * @param artifactDisplayPath
     */
    public void setArtifactDisplayPath(String artifactDisplayPath) {
        this.artifactDisplayPath = artifactDisplayPath;
    }

    /**
     * @return artifact_file_name
     */
    public String getArtifactFileName() {
        return artifactFileName;
    }

    /**
     * @param artifactFileName
     */
    public void setArtifactFileName(String artifactFileName) {
        this.artifactFileName = artifactFileName;
    }

    /**
     * @return artifact_relative_path
     */
    public String getArtifactRelativePath() {
        return artifactRelativePath;
    }

    /**
     * @param artifactRelativePath
     */
    public void setArtifactRelativePath(String artifactRelativePath) {
        this.artifactRelativePath = artifactRelativePath;
    }

    /**
     * @return artifact_size
     */
    public Long getArtifactSize() {
        return artifactSize;
    }

    /**
     * @param artifactSize
     */
    public void setArtifactSize(Long artifactSize) {
        this.artifactSize = artifactSize;
    }

    /**
     * @return storage_path
     */
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * @param storagePath
     */
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
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
}