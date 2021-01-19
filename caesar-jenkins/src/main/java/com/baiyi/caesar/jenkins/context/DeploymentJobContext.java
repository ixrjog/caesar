package com.baiyi.caesar.jenkins.context;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
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
public class DeploymentJobContext implements BaseJobContext{

    @Override
    public int getBuildType(){
        return BuildType.DEPLOYMENT.getType();
    }

    private CsApplication csApplication;

    private CsCiJob csCiJob;

    private CsCdJob csCdJob;

    private JobEngineVO.JobEngine jobEngine;
    private BuildWithDetails buildWithDetails;
    private JobParametersContext jobParamDetail;

    private CdJobBuildVO.JobBuild jobBuild;

}
