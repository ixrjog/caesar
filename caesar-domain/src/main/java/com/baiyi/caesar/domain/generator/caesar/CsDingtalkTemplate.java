package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_dingtalk_template")
public class CsDingtalkTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 通知类型
     */
    @Column(name = "notice_type")
    private Integer noticeType;

    @Column(name = "notice_phase")
    private Integer noticePhase;

    /**
     * 任务类型
     */
    @Column(name = "job_type")
    private String jobType;

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
     * 通知模版
     */
    @Column(name = "notice_template")
    private String noticeTemplate;

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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取通知类型
     *
     * @return notice_type - 通知类型
     */
    public Integer getNoticeType() {
        return noticeType;
    }

    /**
     * 设置通知类型
     *
     * @param noticeType 通知类型
     */
    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * @return notice_phase
     */
    public Integer getNoticePhase() {
        return noticePhase;
    }

    /**
     * @param noticePhase
     */
    public void setNoticePhase(Integer noticePhase) {
        this.noticePhase = noticePhase;
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
     * 获取通知模版
     *
     * @return notice_template - 通知模版
     */
    public String getNoticeTemplate() {
        return noticeTemplate;
    }

    /**
     * 设置通知模版
     *
     * @param noticeTemplate 通知模版
     */
    public void setNoticeTemplate(String noticeTemplate) {
        this.noticeTemplate = noticeTemplate;
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