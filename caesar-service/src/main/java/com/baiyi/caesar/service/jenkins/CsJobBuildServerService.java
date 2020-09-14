package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildServer;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/14 10:45 上午
 * @Version 1.0
 */
public interface CsJobBuildServerService {

    List<CsJobBuildServer> queryCsJobBuildServerByBuildId(int buildType, int buildId);

    void addCsJobBuildServer(CsJobBuildServer csJobBuildServer);

    void deleteCsJobBuildServerById(int id);

}
