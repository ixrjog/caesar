package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildExecutor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/12 3:20 下午
 * @Version 1.0
 */
public interface CsCiJobBuildExecutorService {

    void addCsCiJobBuildExecutor(CsCiJobBuildExecutor csCiJobBuildChange);

    void updateCsCiJobBuildExecutor(CsCiJobBuildExecutor csCiJobBuildChange);

    List<CsCiJobBuildExecutor> queryCsCiJobBuildExecutorByBuildId(int buildId);

    CsCiJobBuildExecutor queryCsCiJobBuildExecutorByUniqueKey(int buildId, String nodeName);
}
