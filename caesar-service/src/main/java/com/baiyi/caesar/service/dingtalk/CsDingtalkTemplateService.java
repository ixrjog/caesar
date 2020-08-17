package com.baiyi.caesar.service.dingtalk;

import com.baiyi.caesar.domain.generator.caesar.CsDingtalkTemplate;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:34 下午
 * @Version 1.0
 */
public interface CsDingtalkTemplateService {

    CsDingtalkTemplate queryCsDingtalkTemplateByUniqueKey(String jobType, int noticeType,int noticePhase);

    void addCsDingtalkTemplate(CsDingtalkTemplate csDingtalkTemplate);

    void updateCsDingtalkTemplate(CsDingtalkTemplate csDingtalkTemplate);

    void deleteCsDingtalkTemplateById(int id);
}
