package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cs_job_build_server")
public class CsJobBuildServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "build_type")
    private Integer buildType;

    @Column(name = "build_id")
    private Integer buildId;

    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 主机分组
     */
    @Column(name = "host_pattern")
    private String hostPattern;

    /**
     * 数据源
     */
    private String source;

    /**
     * 服务器id
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 版本名称
     */
    @Column(name = "version_name")
    private String versionName;

    /**
     * 服务器名称
     */
    @Column(name = "server_name")
    private String serverName;

    /**
     * ip地址
     */
    @Column(name = "private_ip")
    private String privateIp;

    /**
     * 序号
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * 环境
     */
    @Column(name = "env_type")
    private Integer envType;

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
     * @return build_type
     */
    public Integer getBuildType() {
        return buildType;
    }

    /**
     * @param buildType
     */
    public void setBuildType(Integer buildType) {
        this.buildType = buildType;
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
     * @return job_id
     */
    public Integer getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     */
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取主机分组
     *
     * @return host_pattern - 主机分组
     */
    public String getHostPattern() {
        return hostPattern;
    }

    /**
     * 设置主机分组
     *
     * @param hostPattern 主机分组
     */
    public void setHostPattern(String hostPattern) {
        this.hostPattern = hostPattern;
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
     * 获取服务器id
     *
     * @return server_id - 服务器id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取版本名称
     *
     * @return version_name - 版本名称
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * 设置版本名称
     *
     * @param versionName 版本名称
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 获取服务器名称
     *
     * @return server_name - 服务器名称
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * 设置服务器名称
     *
     * @param serverName 服务器名称
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 获取ip地址
     *
     * @return private_ip - ip地址
     */
    public String getPrivateIp() {
        return privateIp;
    }

    /**
     * 设置ip地址
     *
     * @param privateIp ip地址
     */
    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    /**
     * 获取序号
     *
     * @return serial_number - 序号
     */
    public Integer getSerialNumber() {
        return serialNumber;
    }

    /**
     * 设置序号
     *
     * @param serialNumber 序号
     */
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * 获取环境
     *
     * @return env_type - 环境
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * 设置环境
     *
     * @param envType 环境
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
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
}