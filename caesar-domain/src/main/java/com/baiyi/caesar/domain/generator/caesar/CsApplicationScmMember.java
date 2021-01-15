package com.baiyi.caesar.domain.generator.caesar;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cs_application_scm_member")
public class CsApplicationScmMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用id
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * scm类型
     */
    @Column(name = "scm_type")
    private String scmType;

    /**
     * scm id
     */
    @Column(name = "scm_id")
    private Integer scmId;

    @Column(name = "scm_ssh_url")
    private String scmSshUrl;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
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
     * 获取应用id
     *
     * @return application_id - 应用id
     */
    public Integer getApplicationId() {
        return applicationId;
    }

    /**
     * 设置应用id
     *
     * @param applicationId 应用id
     */
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 获取scm类型
     *
     * @return scm_type - scm类型
     */
    public String getScmType() {
        return scmType;
    }

    /**
     * 设置scm类型
     *
     * @param scmType scm类型
     */
    public void setScmType(String scmType) {
        this.scmType = scmType;
    }

    /**
     * 获取scm id
     *
     * @return scm_id - scm id
     */
    public Integer getScmId() {
        return scmId;
    }

    /**
     * 设置scm id
     *
     * @param scmId scm id
     */
    public void setScmId(Integer scmId) {
        this.scmId = scmId;
    }

    /**
     * @return scm_ssh_url
     */
    public String getScmSshUrl() {
        return scmSshUrl;
    }

    /**
     * @param scmSshUrl
     */
    public void setScmSshUrl(String scmSshUrl) {
        this.scmSshUrl = scmSshUrl;
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