package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.domain.param.gitlab.GitlabEventParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsGitlabWebhookMapper extends Mapper<CsGitlabWebhook> {

    List<CsGitlabWebhook> queryCsGitlabWebhookByParam(GitlabEventParam.GitlabEventPageQuery pageQuery);

}