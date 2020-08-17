package com.baiyi.caesar.service.application;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.param.application.ApplicationParam;

/**
 * @Author baiyi
 * @Date 2020/7/21 2:52 下午
 * @Version 1.0
 */
public interface CsApplicationService {

    DataTable<CsApplication> queryCsApplicationByParam(ApplicationParam.ApplicationPageQuery pageQuery);

    DataTable<CsApplication> queryMyCsApplicationByParam(ApplicationParam.MyApplicationPageQuery pageQuery);

    CsApplication queryCsApplicationById(int id);

    void addCsApplication(CsApplication csApplication);

    void updateCsApplication(CsApplication csApplication);

    void deleteCsApplicationById(int id);
}
