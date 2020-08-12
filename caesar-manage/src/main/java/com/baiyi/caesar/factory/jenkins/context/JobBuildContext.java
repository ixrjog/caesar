package com.baiyi.caesar.factory.jenkins.context;

import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
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

    private CsCiJob csCiJob;
    private CsCiJobBuild csCiJobBuild;
    private CiJobVO.JobEngine jobEngine;
    private BuildWithDetails buildWithDetails;
}
