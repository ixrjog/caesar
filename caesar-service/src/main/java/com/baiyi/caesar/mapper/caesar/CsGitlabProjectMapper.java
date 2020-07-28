package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsGitlabProjectMapper extends Mapper<CsGitlabProject> {

    List<CsGitlabProject> queryCsGitlabProjectByParam(GitlabProjectParam.PageQuery pageQuery);
}