package com.baiyi.caesar.builder.aliyun;

import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.baiyi.caesar.bo.AliyunCRInstanceBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.CsAliyunCrInstance;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 5:10 下午
 * @Since 1.0
 */
public class AliyunCRInstanceBuilder {

    public static CsAliyunCrInstance build(ListInstanceResponse.InstancesItem instancesItem) {
        AliyunCRInstanceBO bo = AliyunCRInstanceBO.builder()
                .instanceName(instancesItem.getInstanceName())
                .instanceId(instancesItem.getInstanceId())
                .regionId(instancesItem.getRegionId())
                .build();
        return covert(bo);
    }

    private static CsAliyunCrInstance covert(AliyunCRInstanceBO bo) {
        return BeanCopierUtil.copyProperties(bo, CsAliyunCrInstance.class);
    }
}
