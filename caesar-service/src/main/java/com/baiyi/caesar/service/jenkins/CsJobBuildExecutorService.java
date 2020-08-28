package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildExecutor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/12 3:20 下午
 * @Version 1.0
 */
public interface CsJobBuildExecutorService {

    void addCsJobBuildExecutor(CsJobBuildExecutor csJobBuildChange);

    void updateCsJobBuildExecutor(CsJobBuildExecutor csJobBuildChange);

    List<CsJobBuildExecutor> queryCsJobBuildExecutorByBuildId(int buildType, int buildId);

    CsJobBuildExecutor queryCsJobBuildExecutorByUniqueKey(int buildType, int buildId, String nodeName);
}
