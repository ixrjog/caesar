package com.baiyi.caesar.service.application;

import com.baiyi.caesar.domain.generator.caesar.CsApplicationEngine;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/3 12:54 下午
 * @Version 1.0
 */
public interface CsApplicationEngineService {

    CsApplicationEngine queryCsApplicationEngineByUniqueKey(int applicationId, int jenkinsInstanceId);

    List<CsApplicationEngine> queryCsApplicationEngineByApplicationId(int applicationId);

    List<CsApplicationEngine> selectAll();

    void addCsApplicationEngine(CsApplicationEngine csApplicationEngine);

    void updateCsApplicationEngine(CsApplicationEngine csApplicationEngine);

    void deleteCsApplicationById(int id);
}
