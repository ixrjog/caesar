package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsJobEngine;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/3 4:16 下午
 * @Version 1.0
 */
public interface CsJobEngineService {

    void addCsJobEngine(CsJobEngine csJobEngine);

    void updateCsJobEngine(CsJobEngine csJobEngine);

    void deleteCsJobEngineById(int id);

    List<CsJobEngine> queryCsJobEngineByJobId(int buildType, int jobId);

    CsJobEngine queryCsJobEngineById(int id);

    CsJobEngine queryCsJobEngineByUniqueKey(int buildType, int jobId, int jenkinsInstanceId);
}
