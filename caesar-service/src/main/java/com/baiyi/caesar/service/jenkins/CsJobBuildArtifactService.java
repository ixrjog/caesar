package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/28 2:12 下午
 * @Version 1.0
 */
public interface CsJobBuildArtifactService {

    void addCsJobBuildArtifact(CsJobBuildArtifact csJobBuildArtifact);

    void updateCsJobBuildArtifact(CsJobBuildArtifact csJobBuildArtifact);

    List<CsJobBuildArtifact> queryCsJobBuildArtifactByBuildId(int buildType, int buildId);

    CsJobBuildArtifact queryCsJobBuildArtifactByUniqueKey(int buildId, int buildType, String artifactFileName);

    int countCsJobBuildArtifactByBuildId(int buildType, int buildId);
}
