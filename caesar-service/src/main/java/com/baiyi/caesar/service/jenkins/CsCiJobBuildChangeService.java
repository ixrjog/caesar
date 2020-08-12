package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildChange;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/10 5:16 下午
 * @Version 1.0
 */
public interface CsCiJobBuildChangeService {

    void addCsCiJobBuildChange(CsCiJobBuildChange csCiJobBuildChange);

    void updateCsCiJobBuildChange(CsCiJobBuildChange csCiJobBuildChange);

    List<CsCiJobBuildChange> queryCsCiJobBuildChangeByBuildId(int buildId);
}
