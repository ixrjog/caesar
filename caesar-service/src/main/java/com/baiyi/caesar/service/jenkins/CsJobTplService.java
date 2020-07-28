package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.param.jenkins.JobTplParam;

/**
 * @Author baiyi
 * @Date 2020/7/24 11:38 上午
 * @Version 1.0
 */
public interface CsJobTplService {

    DataTable<CsJobTpl> queryCsJobTplByParam(JobTplParam.JobTplPageQuery pageQuery);

    void addCsJobTpl(CsJobTpl csJobTpl);

    void updateCsJobTpl(CsJobTpl csJobTpl);

    void deleteCsJobTplById(int id);
}
