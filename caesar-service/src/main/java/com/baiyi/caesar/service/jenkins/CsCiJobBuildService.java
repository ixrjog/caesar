package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:09 下午
 * @Version 1.0
 */
public interface CsCiJobBuildService {

    DataTable<CsCiJobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery);

    List<CsCiJobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query);

    CsCiJobBuild queryCiJobBuildById(int id);

    void addCsCiJobBuild(CsCiJobBuild csCiJobBuild);

    void updateCsCiJobBuild(CsCiJobBuild csCiJobBuild);

    void deleteCsCiJobBuildById(int id);

    CsCiJobBuild queryCsCiJobBuildByUniqueKey(int ciJobId, int jobBuildNumber);

    List<CsCiJobBuild> queryCsCiJobBuildByLastSize(int size);

    List<CsCiJobBuild> queryCsCiJobBuildByFinalized(boolean isFinalized);
}
