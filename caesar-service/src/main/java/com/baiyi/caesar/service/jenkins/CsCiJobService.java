package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGoupByWeek;
import com.baiyi.caesar.domain.vo.dashboard.JobTypeTotal;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/29 11:40 上午
 * @Version 1.0
 */
public interface CsCiJobService {

    List<BuildTaskGoupByWeek> queryBuildTaskGoupByWeek();

    List<JobTypeTotal> queryJobTypeTotal();

    DataTable<CsCiJob> queryCsCiJobByParam(CiJobParam.CiJobPageQuery pageQuery);

    DataTable<CsCiJob> queryCsCiJobByParam(CiJobParam.CiJobTplPageQuery pageQuery);

    void addCsCiJob(CsCiJob csCiJob);

    void updateCsCiJob(CsCiJob csCiJob);

    void deleteCsCiJobById(int id);

    CsCiJob queryCsCiJobById(int id);

    List<CsCiJob> queryCsCiJobByScmMemberIdAndBranch(int scmMemberId, String branch);

    List<CsCiJob> queryCsCiJobByJobTplId(int jobTplId);

    List<CsCiJob> selectAll();

    CsCiJob queryCsCiJobByUniqueKey(int applicationId, String jobKey);

    int countCsCiJobByApplicationId(int applicationId);

    int countAllCsCiJob();
}
