package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobEngine;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/3 4:16 下午
 * @Version 1.0
 */
public interface CsCiJobEngineService {

    void addCsCiJobEngine(CsCiJobEngine csCiJobEngine);

    void updateCsCiJobEngine(CsCiJobEngine csCiJobEngine);

    void deleteCsCiJobEngineById(int id);

    List<CsCiJobEngine> queryCsCiJobEngineByJobId(int ciJobId);

    CsCiJobEngine queryCsCiJobEngineByUniqueKey(int ciJobId,int jenkinsInstanceId);
}
