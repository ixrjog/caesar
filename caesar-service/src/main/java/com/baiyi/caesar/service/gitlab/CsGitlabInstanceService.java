package com.baiyi.caesar.service.gitlab;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:33 上午
 * @Version 1.0
 */
public interface CsGitlabInstanceService {

    List<CsGitlabInstance> queryAll();

    CsGitlabInstance queryCsGitlabInstanceById(int id);

    CsGitlabInstance queryCsGitlabInstanceByToken(String token);

    DataTable<CsGitlabInstance> queryCsGitlabInstanceByParam(GitlabInstanceParam.PageQuery pageQuery);

    void addCsGitlabInstance(CsGitlabInstance csGitlabInstance);

    void updateCsGitlabInstance(CsGitlabInstance csGitlabInstance);

    void deleteCsGitlabInstanceById(int id);
}
