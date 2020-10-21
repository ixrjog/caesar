package com.baiyi.caesar.service.gitlab;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import com.baiyi.caesar.domain.param.gitlab.GitlabGroupParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:36 下午
 * @Version 1.0
 */
public interface CsGitlabGroupService {

    DataTable<CsGitlabGroup> queryCsGitlabGroupByParam(GitlabGroupParam.GitlabGroupPageQuery pageQuery);

    List<CsGitlabGroup> queryCsGitlabGroupByInstanceId(Integer instanceId);

    int countCsGitlabGroupByInstanceId(Integer instanceId);

    CsGitlabGroup queryCsGitlabGroupById(int id);

    void addCsGitlabGroup(CsGitlabGroup csGitlabGroup);

    void updateCsGitlabGroup(CsGitlabGroup csGitlabGroup);

    void deleteCsGitlabGroupById(int id);

}
