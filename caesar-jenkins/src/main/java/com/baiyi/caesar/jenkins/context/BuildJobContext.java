package com.baiyi.caesar.jenkins.context;

import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.offbytwo.jenkins.model.BuildWithDetails;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/8/10 4:13 下午
 * @Version 1.0
 */
@Builder
@Data
public class BuildJobContext implements BaseJobContext {

    @Override
    public int getBuildType() {
        return BuildType.BUILD.getType();
    }

    private CsApplication csApplication;
    private CsCiJob csCiJob;
    // SCM
    private CsGitlabProject csGitlabProject;
    private JobEngineVO.JobEngine jobEngine;
    private BuildWithDetails buildWithDetails;
    private JobParametersContext jobParamDetail;


    // 是否回滚
    @Builder.Default
    private boolean isRollback = false;

    private CiJobBuildVO.JobBuild jobBuild;

}
