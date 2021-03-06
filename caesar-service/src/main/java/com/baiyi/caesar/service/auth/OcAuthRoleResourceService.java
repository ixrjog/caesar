package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.generator.caesar.OcAuthRoleResource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:12 下午
 * @Version 1.0
 */
public interface OcAuthRoleResourceService {

    /**
     * 统计被引用的资源条目
     * @param resourceId
     * @return
     */
    int countByResourceId(int resourceId);

    void addOcAuthRoleResource(OcAuthRoleResource ocAuthRoleResource);

    void delOcAuthRoleResourceById(int id);

}
