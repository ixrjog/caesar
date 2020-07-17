package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_jenkins_instance")
public class CsJenkinsInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例名称
     */
    private String name;

    /**
     * 管理url
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * passwordOrToken
     */
    private String token;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

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
     * 获取实例名称
     *
     * @return name - 实例名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置实例名称
     *
     * @param name 实例名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取管理url
     *
     * @return url - 管理url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置管理url
     *
     * @param url 管理url
     */
    public void setUrl(String url) {
        this.url = url;
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
     * 获取passwordOrToken
     *
     * @return token - passwordOrToken
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置passwordOrToken
     *
     * @param token passwordOrToken
     */
    public void setToken(String token) {
        this.token = token;
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
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}