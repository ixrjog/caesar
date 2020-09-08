package com.baiyi.caesar.service.instance;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsInstance;
import com.baiyi.caesar.domain.param.caesar.CaesarInstanceParam;

/**
 * @Author baiyi
 * @Date 2020/9/7 11:00 上午
 * @Version 1.0
 */
public interface CsInstanceService {

    DataTable<CsInstance> queryCsInstanceByParam(CaesarInstanceParam.CaesarInstancePageQuery pageQuery);

    void addCsInstance(CsInstance csInstance);

    void updateCsInstance(CsInstance csInstance);

    CsInstance queryCsInstanceByHostIp(String hostIp);

    CsInstance queryCsInstanceById(int id);
}
