package com.baiyi.caesar.domain.generator.caesar;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cs_application")
public class CsApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用key
     */
    @Column(name = "application_key")
    private String applicationKey;

    /**
     * kubernetes应用id
     */
    @Column(name = "kubernetes_application_id")
    private Integer kubernetesApplicationId;

    /**
     * jenkins引擎类型
     */
    @Column(name = "engine_type")
    private Integer engineType;

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
     * 获取应用名称
     *
     * @return name - 应用名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置应用名称
     *
     * @param name 应用名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取应用key
     *
     * @return application_key - 应用key
     */
    public String getApplicationKey() {
        return applicationKey;
    }

    /**
     * 设置应用key
     *
     * @param applicationKey 应用key
     */
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }

    /**
     * 获取kubernetes应用id
     *
     * @return kubernetes_application_id - kubernetes应用id
     */
    public Integer getKubernetesApplicationId() {
        return kubernetesApplicationId;
    }

    /**
     * 设置kubernetes应用id
     *
     * @param kubernetesApplicationId kubernetes应用id
     */
    public void setKubernetesApplicationId(Integer kubernetesApplicationId) {
        this.kubernetesApplicationId = kubernetesApplicationId;
    }

    /**
     * 获取jenkins引擎类型
     *
     * @return engine_type - jenkins引擎类型
     */
    public Integer getEngineType() {
        return engineType;
    }

    /**
     * 设置jenkins引擎类型
     *
     * @param engineType jenkins引擎类型
     */
    public void setEngineType(Integer engineType) {
        this.engineType = engineType;
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