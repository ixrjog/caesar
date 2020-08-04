package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.param.jenkins.JenkinsInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsJenkinsInstanceMapper extends Mapper<CsJenkinsInstance> {
    List<CsJenkinsInstance> queryCsJenkinsInstanceByParam(JenkinsInstanceParam.JenkinsInstancePageQuery pageQuery);
}