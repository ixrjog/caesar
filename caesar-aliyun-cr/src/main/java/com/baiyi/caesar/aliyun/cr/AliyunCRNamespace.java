package com.baiyi.caesar.aliyun.cr;

import com.aliyuncs.cr.model.v20181201.ListNamespaceResponse;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/15 2:42 下午
 * @Since 1.0
 */
public interface AliyunCRNamespace {

    List<ListNamespaceResponse.NamespacesItem> listNamespace(String regionId, String instanceId);

}
