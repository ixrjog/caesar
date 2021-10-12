package com.baiyi.caesar.aliyun.cr.impl;

import com.aliyuncs.cr.model.v20181201.ListInstanceEndpointResponse;
import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.baiyi.caesar.aliyun.cr.AliyunCRInstance;
import com.baiyi.caesar.aliyun.cr.handler.AliyunCRInstanceHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 4:19 下午
 * @Since 1.0
 */

@Component
public class AliyunCRInstanceImpl implements AliyunCRInstance {

    @Resource
    private AliyunCRInstanceHandler aliyunCRInstanceHandler;

    @Override
    public List<ListInstanceResponse.InstancesItem> listInstance() {
        return aliyunCRInstanceHandler.listInstance();
    }

    @Override
    public ListInstanceEndpointResponse listInstanceEndpoint(String regionId, String instanceId) {
        return aliyunCRInstanceHandler.listInstanceEndpoint(regionId, instanceId);
    }
}
