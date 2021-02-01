package com.baiyi.caesar.service.platform;

import com.baiyi.caesar.domain.generator.caesar.CsBlockPlatform;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/28 9:59 上午
 * @Version 1.0
 */
public interface CsBlockPlatformService {

    List<CsBlockPlatform> queryCsBlockPlatformByValid();
}
