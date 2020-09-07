package com.baiyi.caesar.service.instance;

import com.baiyi.caesar.domain.generator.caesar.CsInstance;

/**
 * @Author baiyi
 * @Date 2020/9/7 11:00 上午
 * @Version 1.0
 */
public interface CsInstanceService {

    void addCsInstance(CsInstance csInstance);

    void updateCsInstance(CsInstance csInstance);

    CsInstance queryCsInstanceByHostIp(String hostIp);
}
