package com.baiyi.caesar.decorator.jenkins.context;

import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.jenkins.JobTplVO;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/12/2 4:16 下午
 * @Version 1.0
 */
@Data
@Builder
public class JobBuildContext {

    private CsOssBucket csOssBucket;

    private CsCiJob csCiJob;

    private CsGitlabProject csGitlabProject;

    private JobTplVO.JobTpl jobTpl;

    @Builder.Default
    private Map<Integer, JobEngineVO.JobEngine> jobEngineMap = Maps.newHashMap();
}
