package com.baiyi.caesar.jenkins.context;

import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
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
public class JobBuildContext {

    private CsApplication csApplication;

    private CsCiJob csCiJob;
    private CiJobBuildVO.JobBuild jobBuild;
    private CiJobVO.JobEngine jobEngine;
    private BuildWithDetails buildWithDetails;
    private JobParamDetail jobParamDetail;

}
