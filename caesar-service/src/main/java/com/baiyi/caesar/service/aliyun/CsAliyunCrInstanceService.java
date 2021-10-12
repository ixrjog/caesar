package com.baiyi.caesar.service.aliyun;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsAliyunCrInstance;
import com.baiyi.caesar.domain.param.aliyun.CrParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 2:37 下午
 * @Since 1.0
 */
public interface CsAliyunCrInstanceService {

    DataTable<CsAliyunCrInstance> csAliyunCrInstancePageQuery(CrParam.InstancePageQuery pageQuery);

    CsAliyunCrInstance getById(int id);

    CsAliyunCrInstance getByInstanceId(String instanceId);

    void add(CsAliyunCrInstance csAliyunCrInstance);

    void update(CsAliyunCrInstance csAliyunCrInstance);

    void deleteById(int id);
}
