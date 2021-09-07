package com.baiyi.caesar.service.dingtalk;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsDingtalk;
import com.baiyi.caesar.domain.param.dingtalk.DingtalkParam;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:23 下午
 * @Version 1.0
 */
public interface CsDingtalkService {

    DataTable<CsDingtalk> queryCsDingtalkByParam(DingtalkParam.DingtalkPageQuery pageQuery);

    CsDingtalk queryCsDingtalkById(int id);

    CsDingtalk getByName(String name);

    void addCsDingtalk(CsDingtalk csDingtalk);

    void updateCsDingtalk(CsDingtalk csDingtalk);

    void deleteCsDingtalkById(int id);
}
