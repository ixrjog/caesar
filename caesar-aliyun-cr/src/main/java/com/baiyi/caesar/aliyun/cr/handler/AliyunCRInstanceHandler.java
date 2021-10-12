package com.baiyi.caesar.aliyun.cr.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.cr.model.v20181201.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.caesar.aliyun.core.AliyunCore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 3:46 下午
 * @Since 1.0
 */

@Component
public class AliyunCRInstanceHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<ListInstanceResponse.InstancesItem> listInstance() {
        try {
            IAcsClient client = aliyunCore.getMasterClient();
            ListInstanceRequest request = new ListInstanceRequest();
            ListInstanceResponse response = client.getAcsResponse(request);
            return response.getInstances();
        } catch (ClientException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public ListInstanceEndpointResponse listInstanceEndpoint(String regionId, String instanceId) {
        try {
            IAcsClient client = aliyunCore.getMasterClient();
            ListInstanceEndpointRequest request = new ListInstanceEndpointRequest();
            request.setRegionId(regionId);
            request.setInstanceId(instanceId);
            ListInstanceEndpointResponse response = client.getAcsResponse(request);
            return response.getIsSuccess() ? response : null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
