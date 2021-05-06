package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_application_resource")
public class CsApplicationResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 资源类型
     */
    @Column(name = "res_type")
    private String resType;

    /**
     * 源
     */
    private String source;

    @Column(name = "res_id")
    private Integer resId;

    /**
     * 资源名称
     */
    @Column(name = "res_name")
    private String resName;

    /**
     * 资源key
     */
    @Column(name = "res_key")
    private String resKey;

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

    @Column(name = "res_comment")
    private String resComment;

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
     * 获取资源类型
     *
     * @return res_type - 资源类型
     */
    public String getResType() {
        return resType;
    }

    /**
     * 设置资源类型
     *
     * @param resType 资源类型
     */
    public void setResType(String resType) {
        this.resType = resType;
    }

    /**
     * 获取源
     *
     * @return source - 源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置源
     *
     * @param source 源
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return res_id
     */
    public Integer getResId() {
        return resId;
    }

    /**
     * @param resId
     */
    public void setResId(Integer resId) {
        this.resId = resId;
    }

    /**
     * 获取资源名称
     *
     * @return res_name - 资源名称
     */
    public String getResName() {
        return resName;
    }

    /**
     * 设置资源名称
     *
     * @param resName 资源名称
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * 获取资源key
     *
     * @return res_key - 资源key
     */
    public String getResKey() {
        return resKey;
    }

    /**
     * 设置资源key
     *
     * @param resKey 资源key
     */
    public void setResKey(String resKey) {
        this.resKey = resKey;
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
     * @return res_comment
     */
    public String getResComment() {
        return resComment;
    }

    /**
     * @param resComment
     */
    public void setResComment(String resComment) {
        this.resComment = resComment;
    }
}