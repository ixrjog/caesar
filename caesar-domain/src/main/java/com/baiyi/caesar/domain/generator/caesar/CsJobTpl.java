package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_job_tpl")
public class CsJobTpl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 实例id
     */
    @Column(name = "jenkins_instance_id")
    private Integer jenkinsInstanceId;

    /**
     * 模版名称
     */
    @Column(name = "tpl_name")
    private String tplName;

    /**
     * 支持回滚
     */
    @Column(name = "support_rollback")
    private Boolean supportRollback;

    /**
     * 0: commit回滚  1: 包回滚
     */
    @Column(name = "rollback_type")
    private Integer rollbackType;

    /**
     * 模版类型
     */
    @Column(name = "tpl_type")
    private String tplType;

    /**
     * 部署任务id
     */
    @Column(name = "tpl_version")
    private Integer tplVersion;

    @Column(name = "tpl_hash")
    private String tplHash;

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
     * 模版内容
     */
    @Column(name = "tpl_content")
    private String tplContent;

    @Column(name = "parameter_yaml")
    private String parameterYaml;

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
     * 获取实例id
     *
     * @return jenkins_instance_id - 实例id
     */
    public Integer getJenkinsInstanceId() {
        return jenkinsInstanceId;
    }

    /**
     * 设置实例id
     *
     * @param jenkinsInstanceId 实例id
     */
    public void setJenkinsInstanceId(Integer jenkinsInstanceId) {
        this.jenkinsInstanceId = jenkinsInstanceId;
    }

    /**
     * 获取模版名称
     *
     * @return tpl_name - 模版名称
     */
    public String getTplName() {
        return tplName;
    }

    /**
     * 设置模版名称
     *
     * @param tplName 模版名称
     */
    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    /**
     * 获取支持回滚
     *
     * @return support_rollback - 支持回滚
     */
    public Boolean getSupportRollback() {
        return supportRollback;
    }

    /**
     * 设置支持回滚
     *
     * @param supportRollback 支持回滚
     */
    public void setSupportRollback(Boolean supportRollback) {
        this.supportRollback = supportRollback;
    }

    /**
     * 获取0: commit回滚  1: 包回滚
     *
     * @return rollback_type - 0: commit回滚  1: 包回滚
     */
    public Integer getRollbackType() {
        return rollbackType;
    }

    /**
     * 设置0: commit回滚  1: 包回滚
     *
     * @param rollbackType 0: commit回滚  1: 包回滚
     */
    public void setRollbackType(Integer rollbackType) {
        this.rollbackType = rollbackType;
    }

    /**
     * 获取模版类型
     *
     * @return tpl_type - 模版类型
     */
    public String getTplType() {
        return tplType;
    }

    /**
     * 设置模版类型
     *
     * @param tplType 模版类型
     */
    public void setTplType(String tplType) {
        this.tplType = tplType;
    }

    /**
     * 获取部署任务id
     *
     * @return tpl_version - 部署任务id
     */
    public Integer getTplVersion() {
        return tplVersion;
    }

    /**
     * 设置部署任务id
     *
     * @param tplVersion 部署任务id
     */
    public void setTplVersion(Integer tplVersion) {
        this.tplVersion = tplVersion;
    }

    /**
     * @return tpl_hash
     */
    public String getTplHash() {
        return tplHash;
    }

    /**
     * @param tplHash
     */
    public void setTplHash(String tplHash) {
        this.tplHash = tplHash;
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
     * 获取模版内容
     *
     * @return tpl_content - 模版内容
     */
    public String getTplContent() {
        return tplContent;
    }

    /**
     * 设置模版内容
     *
     * @param tplContent 模版内容
     */
    public void setTplContent(String tplContent) {
        this.tplContent = tplContent;
    }

    /**
     * @return parameter_yaml
     */
    public String getParameterYaml() {
        return parameterYaml;
    }

    /**
     * @param parameterYaml
     */
    public void setParameterYaml(String parameterYaml) {
        this.parameterYaml = parameterYaml;
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