package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/17 3:45 下午
 * @Version 1.0
 */
public interface CsJenkinsInstanceService {

    List<CsJenkinsInstance> queryAll();
}
