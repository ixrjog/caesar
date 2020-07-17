package com.baiyi.caesar.service.ram;

import com.baiyi.caesar.domain.generator.caesar.OcAliyunRamPermission;
import com.baiyi.caesar.domain.generator.caesar.OcAliyunRamUser;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 6:16 下午
 * @Version 1.0
 */
public interface OcAliyunRamPermissionService {

    void addOcAliyunRamPermission(OcAliyunRamPermission ocAliyunRamPermission);

    void deleteOcAliyunRamPermissionById(int id);

    OcAliyunRamPermission queryOcAliyunRamPermissionByUniqueKey(OcAliyunRamPermission ocAliyunRamPermission);

    List<OcAliyunRamPermission> queryOcAliyunRamPermissionByOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser);
}
