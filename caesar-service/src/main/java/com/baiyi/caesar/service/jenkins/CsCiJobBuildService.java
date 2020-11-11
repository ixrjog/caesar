package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;
import com.baiyi.caesar.domain.vo.dashboard.HotApplication;
import com.baiyi.caesar.domain.vo.dashboard.HotUser;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:09 下午
 * @Version 1.0
 */
public interface CsCiJobBuildService {

    List<HotApplication> queryHotApplication(int length);

    List<HotUser> queryHotUser(int length);

    List<BuildTaskGroupByHour> queryCiJobBuildGroupByHour();

    List<CsCiJobBuild> queryLatestCsCiJobBuild(int length);

    int countAllCsCiJobBuild();

    DataTable<CsCiJobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery);

    List<CsCiJobBuild> queryCiJobBuildByCiJobId(int ciJobId);

    List<CsCiJobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query);

    CsCiJobBuild queryCiJobBuildById(int id);

    List<CsCiJobBuild> queryLatestCiJobBuildByCiJobId(int ciJobId);

    void addCsCiJobBuild(CsCiJobBuild csCiJobBuild);

    void updateCsCiJobBuild(CsCiJobBuild csCiJobBuild);

    void deleteCsCiJobBuildById(int id);

    CsCiJobBuild queryCsCiJobBuildByUniqueKey(int ciJobId, int jobBuildNumber);

    List<CsCiJobBuild> queryCsCiJobBuildByLastSize(int size);

    List<CsCiJobBuild> queryCsCiJobBuildByFinalized(boolean isFinalized);
}
