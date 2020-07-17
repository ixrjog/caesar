package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.domain.generator.caesar.OcCloudVpc;
import com.baiyi.caesar.facade.CloudVpcBaseFacade;
import com.baiyi.caesar.service.cloud.OcCloudVpcSecurityGroupService;
import com.baiyi.caesar.service.cloud.OcCloudVpcService;
import com.baiyi.caesar.service.cloud.OcCloudVpcVswitchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:54 上午
 * @Version 1.0
 */
@Service
public class CloudVpcBaseFacadeImpl implements CloudVpcBaseFacade {

    @Resource
    private OcCloudVpcService ocCloudVpcService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    /**
     * 删除VPC VSW SG
     * @param ocCloudVpc
     */
    public void deleteOcCloudVpc(OcCloudVpc ocCloudVpc){
        String vpcId = ocCloudVpc.getVpcId();
        // 删除所有虚拟交换机
        ocCloudVpcVswitchService.deleteOcCloudVpcVswitchByVpcId(vpcId);
        // 删除SG
        ocCloudVpcSecurityGroupService.deleteOcCloudVpcSecurityGroupByVpcId(vpcId);
        // 删除VPC
        ocCloudVpcService.deleteOcCloudVpcById(ocCloudVpc.getId());
    }

}
