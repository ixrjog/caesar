package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:09 下午
 * @Version 1.0
 */
public interface CsCiJobBuildService {

    void addCsCiJobBuild(CsCiJobBuild csCiJobBuild);

    void updateCsCiJobBuild(CsCiJobBuild csCiJobBuild);

    void deleteCsCiJobBuildById(int id);

    CsCiJobBuild queryCsCiJobBuildByUniqueKey(int ciJobId, int jobBuildNumber);

    List<CsCiJobBuild> queryCsCiJobBuildByLastSize(int size);
}
