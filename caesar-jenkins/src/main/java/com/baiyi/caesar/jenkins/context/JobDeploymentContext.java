package com.baiyi.caesar.jenkins.context;

import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.offbytwo.jenkins.model.BuildWithDetails;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/8/28 10:10 上午
 * @Version 1.0
 */
@Builder
@Data
public class JobDeploymentContext {

    @Builder.Default
    private int buildType = 1;

    private CsApplication csApplication;
    private CsCdJob csCdJob;

    private CiJobVO.JobEngine jobEngine;
    private BuildWithDetails buildWithDetails;
    private JobParamDetail jobParamDetail;

    private CdJobBuildVO.JobBuild jobBuild;

}
