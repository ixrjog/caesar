package com.baiyi.caesar.aliyun.cr.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.cr.model.v20181201.ListNamespaceRequest;
import com.aliyuncs.cr.model.v20181201.ListNamespaceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.caesar.aliyun.core.AliyunCore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/15 2:36 下午
 * @Since 1.0
 */

@Component
public class AliyunCRNamespaceHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<ListNamespaceResponse.NamespacesItem> listNamespace(String regionId, String instanceId) {
        try {
            IAcsClient client = aliyunCore.getMasterClient();
            ListNamespaceRequest request = new ListNamespaceRequest();
            request.setRegionId(regionId);
            request.setInstanceId(instanceId);
            ListNamespaceResponse response = client.getAcsResponse(request);
            return response.getNamespaces();
        } catch (ClientException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
