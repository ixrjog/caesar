package com.baiyi.caesar.service.jenkins;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/7 5:56 下午
 * @Version 1.0
 */
public interface CsCiJobBuildArtifactService {

    void addCsCiJobBuildArtifact(CsCiJobBuildArtifact csCiJobBuildArtifact);

    void updateCsCiJobBuildArtifact(CsCiJobBuildArtifact csCiJobBuildArtifact);

    List<CsCiJobBuildArtifact> queryCsCiJobBuildArtifactByBuildId(int buildId);

    CsCiJobBuildArtifact queryCsCiJobBuildArtifactByUniqueKey(int buildId,String artifactFileName);
}
