package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.param.application.CdJobParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/26 2:45 下午
 * @Version 1.0
 */
public interface CsCdJobService {

    DataTable<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobPageQuery pageQuery);

    DataTable<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobTplPageQuery pageQuery);

    List<CsCdJob> selectAll();

    void addCsCdJob(CsCdJob csCdJb);

    void updateCsCdJob(CsCdJob csCdJob);

    void deleteCsCdJobById(int id);

    CsCdJob queryCsCdJobByUniqueKey(int applicationId, String jobKey);

    CsCdJob queryCsCdJobById(int id);

    int countCsCdJobByApplicationId(int applicationId);

    int countAllCsCdJob();
}
