package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcAuthResource;
import com.baiyi.caesar.domain.param.auth.ResourceParam;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:05 下午
 * @Version 1.0
 */
public interface OcAuthResourceService {

    int countByGroupId(int groupId);

    OcAuthResource queryOcAuthResourceByName(String resourceName);

    DataTable<OcAuthResource> queryOcAuthResourceByParam(ResourceParam.PageQuery pageQuery);

    void addOcAuthResource(OcAuthResource ocAuthResource);

    void updateOcAuthResource(OcAuthResource ocAuthResource);

    void deleteOcAuthResourceById(int id);

    OcAuthResource queryOcAuthResourceById(int id);

    // 角色绑定的资源
    DataTable<OcAuthResource> queryRoleBindOcAuthResourceByParam(ResourceParam.BindResourcePageQuery pageQuery);

    DataTable<OcAuthResource> queryRoleUnbindOcAuthResourceByParam(ResourceParam.BindResourcePageQuery pageQuery);

}
