package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsGitlabInstanceMapper extends Mapper<CsGitlabInstance> {

    List<CsGitlabInstance> queryCsGitlabInstanceByParam(GitlabInstanceParam.PageQuery pageQuery);
}