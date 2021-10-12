package com.baiyi.caesar.domain.generator.caesar;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cs_aliyun_cr_instance")
public class CsAliyunCrInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "instance_name")
    private String instanceName;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "internet_endpoint")
    private String internetEndpoint;

    @Column(name = "intranet_endpoint")
    private String intranetEndpoint;

    @Column(name = "vpc_endpoint")
    private String vpcEndpoint;

    @Column(name = "create_time")
    private Date createTime;

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
     * @return region_id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * @param regionId
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * @return instance_name
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * @param instanceName
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * @return instance_id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @param instanceId
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return is_active
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return internet_endpoint
     */
    public String getInternetEndpoint() {
        return internetEndpoint;
    }

    /**
     * @param internetEndpoint
     */
    public void setInternetEndpoint(String internetEndpoint) {
        this.internetEndpoint = internetEndpoint;
    }

    /**
     * @return intranet_endpoint
     */
    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    /**
     * @param intranetEndpoint
     */
    public void setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
    }

    /**
     * @return vpc_endpoint
     */
    public String getVpcEndpoint() {
        return vpcEndpoint;
    }

    /**
     * @param vpcEndpoint
     */
    public void setVpcEndpoint(String vpcEndpoint) {
        this.vpcEndpoint = vpcEndpoint;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}