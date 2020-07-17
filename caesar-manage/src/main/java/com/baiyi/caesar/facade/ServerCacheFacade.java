package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;

/**
 * @Author baiyi
 * @Date 2020/4/10 10:19 上午
 * @Version 1.0
 */
public interface ServerCacheFacade {

    void evictServerCache(OcServer ocServer);

    void evictServerGroupCache(OcServerGroup ocServerGroup);
}
