package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildChange;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/10 5:16 下午
 * @Version 1.0
 */
public interface CsJobBuildChangeService {

    void addCsJobBuildChange(CsJobBuildChange csJobBuildChange);

    void updateCsJobBuildChange(CsJobBuildChange csJobBuildChange);

    List<CsJobBuildChange> queryCsJobBuildChangeByBuildId(int buildType,int buildId);

    CsJobBuildChange queryCsJobBuildChangeByUniqueKey(int buildType,int jobId, String commitId);
}
