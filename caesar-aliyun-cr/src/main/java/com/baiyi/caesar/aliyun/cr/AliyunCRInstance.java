package com.baiyi.caesar.aliyun.cr;

import com.aliyuncs.cr.model.v20181201.ListInstanceEndpointResponse;
import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 4:19 下午
 * @Since 1.0
 */
public interface AliyunCRInstance {

    List<ListInstanceResponse.InstancesItem> listInstance();

    ListInstanceEndpointResponse listInstanceEndpoint(String regionId, String instanceId);
}
