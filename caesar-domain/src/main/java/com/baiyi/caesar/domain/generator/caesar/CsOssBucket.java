package com.baiyi.caesar.domain.generator.caesar;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cs_oss_bucket")
public class CsOssBucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * Bucket所在的数据中心
     */
    @Column(name = "bucket_location")
    private String bucketLocation;

    /**
     * Bucket访问的外网域名
     */
    @Column(name = "extranet_endpoint")
    private String extranetEndpoint;

    /**
     * Bucket的内网域名
     */
    @Column(name = "intranet_endpoint")
    private String intranetEndpoint;

    @Column(name = "bucket_region")
    private String bucketRegion;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

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
     * 获取Bucket所在的数据中心
     *
     * @return bucket_location - Bucket所在的数据中心
     */
    public String getBucketLocation() {
        return bucketLocation;
    }

    /**
     * 设置Bucket所在的数据中心
     *
     * @param bucketLocation Bucket所在的数据中心
     */
    public void setBucketLocation(String bucketLocation) {
        this.bucketLocation = bucketLocation;
    }

    /**
     * 获取Bucket访问的外网域名
     *
     * @return extranet_endpoint - Bucket访问的外网域名
     */
    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    /**
     * 设置Bucket访问的外网域名
     *
     * @param extranetEndpoint Bucket访问的外网域名
     */
    public void setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
    }

    /**
     * 获取Bucket的内网域名
     *
     * @return intranet_endpoint - Bucket的内网域名
     */
    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    /**
     * 设置Bucket的内网域名
     *
     * @param intranetEndpoint Bucket的内网域名
     */
    public void setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
    }

    /**
     * @return bucket_region
     */
    public String getBucketRegion() {
        return bucketRegion;
    }

    /**
     * @param bucketRegion
     */
    public void setBucketRegion(String bucketRegion) {
        this.bucketRegion = bucketRegion;
    }

    /**
     * 获取有效
     *
     * @return is_active - 有效
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * 设置有效
     *
     * @param isActive 有效
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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