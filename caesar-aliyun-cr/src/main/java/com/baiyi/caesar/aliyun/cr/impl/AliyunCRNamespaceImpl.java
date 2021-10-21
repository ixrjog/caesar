package com.baiyi.caesar.aliyun.cr.impl;

import com.aliyuncs.cr.model.v20181201.ListNamespaceResponse;
import com.baiyi.caesar.aliyun.cr.AliyunCRNamespace;
import com.baiyi.caesar.aliyun.cr.handler.AliyunCRNamespaceHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/15 2:43 下午
 * @Since 1.0
 */

@Component
public class AliyunCRNamespaceImpl implements AliyunCRNamespace {

    @Resource
    private AliyunCRNamespaceHandler aliyunCRNamespaceHandler;

    @Override
    public List<ListNamespaceResponse.NamespacesItem> listNamespace(String regionId, String instanceId) {
        return aliyunCRNamespaceHandler.listNamespace(regionId, instanceId);
    }

}
