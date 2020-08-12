package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_ci_job_build_change")
public class CsCiJobBuildChange {
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

    @Column(name = "commit_id")
    private String commitId;

    @Column(name = "commit_date")
    private Date commitDate;

    @Column(name = "author_full_name")
    private String authorFullName;

    @Column(name = "author_absolute_url")
    private String authorAbsoluteUrl;

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

    private String commit;

    @Column(name = "commit_msg")
    private String commitMsg;

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
     * @return commit_id
     */
    public String getCommitId() {
        return commitId;
    }

    /**
     * @param commitId
     */
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    /**
     * @return commit_date
     */
    public Date getCommitDate() {
        return commitDate;
    }

    /**
     * @param commitDate
     */
    public void setCommitDate(Date commitDate) {
        this.commitDate = commitDate;
    }

    /**
     * @return author_full_name
     */
    public String getAuthorFullName() {
        return authorFullName;
    }

    /**
     * @param authorFullName
     */
    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    /**
     * @return author_absolute_url
     */
    public String getAuthorAbsoluteUrl() {
        return authorAbsoluteUrl;
    }

    /**
     * @param authorAbsoluteUrl
     */
    public void setAuthorAbsoluteUrl(String authorAbsoluteUrl) {
        this.authorAbsoluteUrl = authorAbsoluteUrl;
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
     * @return commit_msg
     */
    public String getCommitMsg() {
        return commitMsg;
    }

    /**
     * @param commitMsg
     */
    public void setCommitMsg(String commitMsg) {
        this.commitMsg = commitMsg;
    }
}