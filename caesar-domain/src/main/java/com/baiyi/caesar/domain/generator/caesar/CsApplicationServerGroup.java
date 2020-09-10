package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_application_server_group")
public class CsApplicationServerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用id
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 数据源
     */
    private String source;

    /**
     * 服务器组id
     */
    @Column(name = "server_group_id")
    private Integer serverGroupId;

    /**
     * 服务器组名称
     */
    @Column(name = "server_group_name")
    private String serverGroupName;

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
     * 获取数据源
     *
     * @return source - 数据源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置数据源
     *
     * @param source 数据源
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取服务器组id
     *
     * @return server_group_id - 服务器组id
     */
    public Integer getServerGroupId() {
        return serverGroupId;
    }

    /**
     * 设置服务器组id
     *
     * @param serverGroupId 服务器组id
     */
    public void setServerGroupId(Integer serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    /**
     * 获取服务器组名称
     *
     * @return server_group_name - 服务器组名称
     */
    public String getServerGroupName() {
        return serverGroupName;
    }

    /**
     * 设置服务器组名称
     *
     * @param serverGroupName 服务器组名称
     */
    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
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