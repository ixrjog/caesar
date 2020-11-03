package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.param.application.CdJobParam;

/**
 * @Author baiyi
 * @Date 2020/8/26 2:45 下午
 * @Version 1.0
 */
public interface CsCdJobService {

    DataTable<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobPageQuery pageQuery);

    DataTable<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobTplPageQuery pageQuery);


    void addCsCdJob(CsCdJob csCdJob);

    void updateCsCdJob(CsCdJob csCdJob);

    void deleteCsCdJobById(int id);

    CsCdJob queryCsCdJobByUniqueKey(int applicationId, String jobKey);

    CsCdJob queryCsCdJobById(int id);
}
