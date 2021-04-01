package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/29 2:21 下午
 * @Version 1.0
 */
public interface CsCdJobBuildService {

    List<BuildTaskGroupByHour> queryCdJobBuildGroupByHour();

    List<CsCdJobBuild> queryLatestCsCdJobBuild(int length);

    int countAllCsCdJobBuild();

    DataTable<CsCdJobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery);

    List<CsCdJobBuild> queryCdJobBuildByCdJobId(int cdJobId);

    List<CsCdJobBuild> queryLastCdJobBuild(int cdJobId);

    CsCdJobBuild queryCdJobBuildById(int id);

    void addCsCdJobBuild(CsCdJobBuild csCdJobBuild);

    void updateCsCdJobBuild(CsCdJobBuild csCdJobBuild);

    void deleteCsCdJobBuildById(int id);

    List<CsCdJobBuild> queryMyCdJobBuild(String username, int size);

    CsCdJobBuild queryCsCdJobBuildByUniqueKey(int cdJobId, int jobBuildNumber);

    List<CsCdJobBuild> queryCsCdJobBuildByLastSize(int size);

    List<CsCdJobBuild> queryCsCdJobBuildByFinalized(boolean isFinalized);
}
