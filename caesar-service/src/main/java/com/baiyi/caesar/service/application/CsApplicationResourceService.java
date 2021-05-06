package com.baiyi.caesar.service.application;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationResource;

/**
 * @Author baiyi
 * @Date 2021/5/6 1:27 下午
 * @Version 1.0
 */
public interface CsApplicationResourceService {

    void addApplicationResource(CsApplicationResource csApplicationResource);

    void updateApplicationResource(CsApplicationResource csApplicationResource);

    CsApplicationResource queryApplicationResourceByUniqueKey(int applicationId, String resType, String resKey);
}
